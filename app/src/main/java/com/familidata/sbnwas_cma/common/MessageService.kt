package com.familidata.sbnwas_cma.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.ActionPatternData
import com.familidata.sbnwas_cma.api.req.BioTag
import com.familidata.sbnwas_cma.api.req.RequestBioLog
import com.familidata.sbnwas_cma.common.job.inwork.InWorkWatchCheckExecutor
import com.familidata.sbnwas_cma.common.job.prework.PreWorkWatchCheckExecutor
import com.familidata.sbnwas_cma.main.MainActivity
import com.familidata.sbnwas_cma.main.workstatus.WatchConnector
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.*
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.familidata.sbnwas_cma.util.CommonUtil.toDoubleStringData
import com.familidata.sbnwas_cma.util.CommonUtil.toIntStringData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class MessageService : Service() {
    private var json: JSONObject? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val messageClient = OnMessageReceivedListener {
        executor.execute {
            receiveData(it)
//            Log.d("==========>스레드이름", Thread.currentThread().name)
        }
    }

    private fun receiveData(it: MessageEvent) {

        if ("" == DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)) {
            return
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)

        if (it.path == SBNWAS_MESSAGE_PATH) {

            val bytes = it.data
            val data = String(bytes)

            if ("profileInfo" == data) {
                // watch -> mobile -> watch (체성분 측정을 위해 사용자 정보 연계)
                sendProfileToWatch()

            } else {
                // watch -> mobile

                // get json from data
                val json = createJsonFromData(data)

                json?.let {

                    var list: MutableList<ISuperLogEntity> = ArrayList()
                    var list2: MutableList<Any> = ArrayList()

                    var isNeedBus: Boolean = false

                    var topic: String = it!!.get("topic").toString()

                    when(topic) {
                        "bio_htr" -> {
                            list = DBManager.withDB().withBioHtr().createHtrMessageDatas(topic, formatted, it.getJSONArray("datas"))
                            isNeedBus = true
                        }
                        "bio_oxs" -> {
                            list = DBManager.withDB().withBioOxs().createOxsMessageDatas(topic, formatted, it)
                            isNeedBus = true
                        }
                        "bio_bdy" -> {
                            list = DBManager.withDB().withBioBdy().createBdyMessageDatas(topic, formatted, it)
                            isNeedBus = true
                        }
                        "bio_tag" -> {
                            list2 = createTagDatas(topic, formatted, it)
                            isNeedBus = false
                        }
                        "bio_acc" -> {
                            list = DBManager.withDB().withBioAcc().createAccMessageDatas(topic, formatted, it)
                            isNeedBus = false
                        }
                        "bio_ecg" -> {
                            list = DBManager.withDB().withBioEcg().createEcgMessageDatas(topic, formatted, it)
                            isNeedBus = true
                        }
                        "bio_gyr" -> {
                            list = DBManager.withDB().withBioGyr().createGyrMessageDatas(topic, formatted, it)
                            isNeedBus = false
                        }
                        "bio_gps" -> {
                            list = DBManager.withDB().withBioGps().createGpsMessageDatas(topic, formatted, it)
                            isNeedBus = false
                        }
                        "action_pattern" -> {
                            list2 = createActionPatternMessageDatas(topic, formatted, it)
                            isNeedBus = false
                        }
                        "bio_htr_auto" -> {
                            list = createHtrAutoMessageDatas(topic, formatted, it.getJSONArray("datas"))
                            isNeedBus = false
                        }
                        "status" -> {
                            isNeedBus = true
                        }
                        "watch_status" -> {
                            checkWatchStatus(it)
                            isNeedBus = true
                        }
                        else -> {  }
                    } // when

                    if (list.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            CommonApi().sendBioLog(EntityConvertor.entitiyToReqDatas(topic, list))
                        }
                    }

                    if (list2.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            CommonApi().sendBioLog(RequestBioLog(topic = topic, datas = list2))
                        }
                    }

                    if (isNeedBus) {

                        when(topic) {
                            "bio_htr" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.BIO_WATCH_HTR, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
                                )
                            }
                            "bio_oxs" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.BIO_WATCH_OXY, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
                                )
                            }
                            "bio_ecg" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.BIO_WATCH_ECG, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
                                )
                            }
                            "bio_bdy" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.BIO_WATCH_BDY, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
                                )
                            }
                            "status" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.WORK_START_RES, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
                                )
                            }
                            else -> {
                            }
                        } // when

                        when(topic) {
                            "bio_htr", "bio_oxs", "bio_ecg", "bio_bdy" -> {
                                CmaApplication.instance.bus()?.send(
                                    Pair(RxBusObj.MAIN_PAGE_0, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)
                                    )
                                )
                            }
                            else -> {
                            }
                        } // when

                    } // if

                }

            }

//            Log.d(TAG, json.toString());

        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("================>", "서비스 생성")
    }

    override fun onDestroy() {
        super.onDestroy()
        //ID = null
        stopForeground(true)
        stopSelf()
        executor.shutdown()
//        MainActivity.prefs.setBoolean("running", false)
        Wearable.getMessageClient(this).removeListener(messageClient)
        //Wearable.getDataClient(this).removeListener(ecgDataClient)
        WorkManager.getInstance(this).cancelAllWork()
        Log.d("======================>", "스케줄러 종료")
        Log.d("======================>", "포어그라운드 서비스 종료")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent == null) {
            Log.d(TAG, "아이디없음")
        } else {
            if (intent.hasExtra("logout")) {
                Log.d("======================>", "로그아웃 호출")
                //ID = null
                stopForeground(true)
                stopSelf()
            } else {
                Log.d(TAG, "아이디있음")
//                MainActivity.prefs.setBoolean("running", true)
                //ID = intent.getStringExtra("id")
                Log.d("======================>", "포어그라운드 서비스 시작")
                createNotification()
                Wearable.getMessageClient(this).addListener(messageClient)
                //Wearable.getDataClient(this).addListener(ecgDataClient)

            }

        }
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2 * 1000
        return START_REDELIVER_INTENT
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
        builder.setSmallIcon(R.mipmap.app_icon)
        builder.setOngoing(true)
        builder.setContentTitle(getString(R.string.app_name))
        builder.setContentText(getString(R.string.msg_service_on))
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)

        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel(
            getString(R.string.app_name), getString(R.string.basic_channel), NotificationManager.IMPORTANCE_HIGH
        ).apply { setShowBadge(false) })
        notificationManager.notify(NOTI_ID, builder.build())
        val notification = builder.build()
        startForeground(NOTI_ID, notification)
    }


    /**
     * Watch 체성분 측정을 위한 기초 데이터 전송
     */
    private fun sendProfileToWatch() {

        val json = JSONObject()
        json.put("PROFILE", "Y")
        json.put(PropertyObj.GENDER, DBManager.withDB().withProperty().getProperty(PropertyObj.GENDER))
        json.put(PropertyObj.HEIGHT, toDoubleStringData(DBManager.withDB().withProperty().getProperty(PropertyObj.HEIGHT)))
        json.put(PropertyObj.WEIGHT, toDoubleStringData(DBManager.withDB().withProperty().getProperty(PropertyObj.WEIGHT)))

        executor.execute(WatchConnector(context = this, json, executor).checkConnection())
        
    }

    /**
     * Watch 수신 데이터 parsing
     */
    private fun createJsonFromData(data: String) : JSONObject? {

        var json: JSONObject? = null
        try {
            json = JSONObject(data)
        } catch (e: Exception) {
            Log.d("createJsonFromData", e.printStackTrace())
            json = JSONObject()
        }
        return json
    }



    /**
     * 1. Tag 데이터 parsing
     * 2. Tag 처리
     */
    private fun createTagDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<Any> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<Any> = ArrayList()


        var data = BioTag(
            userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            get_time = getTime,
            bio_type = item.getString("bio_type"),
            tag1 = if (item.has("tag_1")) item.getString("tag_1") else null,
            tag2 = if (item.has("tag_2")) item.getString("tag_2") else null,
            tag3 = if (item.has("tag_3")) item.getString("tag_3") else null,
            tag4 = if (item.has("tag_4")) item.getString("tag_4") else null,
            tag5 = if (item.has("tag_5")) item.getString("tag_5") else null,
        )

        if ("htr" == data.bio_type) {
            DBManager.withDB().withBioHtr().updateTag(data.userId, data.get_time, data.tag1!!)
        } else if ("oxs" == data.bio_type) {
            DBManager.withDB().withBioOxs().updateTag(data.userId, data.get_time, data.tag1!!)
        }

        list.add(data)

        return list
    }

    /**
     * 1. Action pattern 데이터 parsing
     */
    private fun createActionPatternMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<Any> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<Any> = ArrayList()

        var data = ActionPatternData(
            userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            start = item.getString("start"),
            end = item.getString("end"),
            type = item.getString("type"),
            sensor = item.getString("sensor"),
        )

        list.add(data)

        return list
    }

    /**
     * 1. 심박수 자동 측정 데이터 parsing
     */
    private fun createHtrAutoMessageDatas(topic: String, formattedNow: String, datas: JSONArray) : MutableList<ISuperLogEntity> {

        var getTime: String = ""
        val list: MutableList<ISuperLogEntity> = ArrayList()

        for (i in 0 until datas.length()) {
            val item = datas.getJSONObject(i)
            var data = BioHtr(
                GET_TIME = item.getString("get_time"),
                DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                HEART_RATE = toIntStringData(item.getString("heart_rate")),   // 심박수는 정수 처리
                TAG1 = "",
                DEVICE_SERIAL = "",
                CREATE_DTTM = formattedNow,
                SAVE_TO_SERVER_YN = "N",
            )
            list.add(data)
        } // end for

        return list
    }

    /**
     * Watch 상태 점검
     */
    private fun checkWatchStatus(watchStatus: JSONObject) {

        Log.i("워치 가 답변함.")
        Log.i(watchStatus)

        val isOnPreWork: Boolean = DBManager.withDB().withWorkPlan().isOnPreWork();
        val isOnWork: Boolean = DBManager.withDB().withWorkPlan().isOnWork();

        if (isOnPreWork) {
            PreWorkWatchCheckExecutor.SEND_RESULT = 1
        } else if (isOnWork) {
            InWorkWatchCheckExecutor.SEND_RESULT = 1
        }
        
        // 근무를 위한 watch 착용 여부 점검
        if ((isOnPreWork || isOnWork) && "N" == watchStatus.getString("WEAR_YN")) {
            if (isOnWork) {
                CommonUtil.createNotificationChannel(
                    this, getString(R.string.msg_non_wear_watch), getString(R.string.msg_wear_watch_on_work),
                )
            } else {
                CommonUtil.createNotificationChannel(
                    this, getString(R.string.msg_non_wear_watch), getString(R.string.msg_wear_watch_before_work),
                )
            }
        }

        // 근무를 위한 watch 배터리 상태 점검
        if (isOnPreWork && watchStatus.getString("BATTERY").toFloat() < 70f) {
            CommonUtil.createNotificationChannel(
                this, getString(R.string.msg_watch_battery), getString(R.string.msg_watch_batter_under_70)
            )
        }

        // 근무중 watch sensor 상태 점검
        if (isOnWork) {
            if (DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_ACC) != watchStatus.getString("WI_BIO_ACC")
                || DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GYR) != watchStatus.getString("WI_BIO_GYR")
                || DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GPS) != watchStatus.getString("WI_BIO_GPS")
            ) {
                InWorkWatchCheckExecutor.SEND_RESULT = 0
            }
        }

    }

    companion object {
        private const val TAG = "MessageListener"
        private const val SBNWAS_MESSAGE_PATH = "/sbnwas"
        private const val SBNWAS_ACC_MESSAGE_PATH = "/sbnwas/acc"
        private const val NOTI_ID = 1
    }
}