package com.familidata.sbnwas_cma.api.common

import androidx.appcompat.app.AppCompatActivity
import com.familidata.base.Log
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.*
import com.familidata.sbnwas_cma.api.res.*
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.*
import com.familidata.sbnwas_cma.util.CommonUtil
import com.familidata.sbnwas_cma.util.CommonUtil.toDoubleStringData
import com.familidata.sbnwas_cma.util.CommonUtil.toIntStringData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class CommonApi : AppCompatActivity() {

    //사용가능한 장비 리스트 가져오기
    fun getDeviceList(paramFunc: (Boolean) -> Unit) {
        ApiService.getDeviceConfig(
            RequestUserID(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            )
        ).enqueue(object : Callback<ResponseDeviceConfigData> {
            override fun onResponse(call: Call<ResponseDeviceConfigData>, response: Response<ResponseDeviceConfigData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }
                            it.datas?.let { it2 ->
                                DBManager.withDB().withDeviceConfig().resetAll()
                                DBManager.withDB().withProperty().setProperty(PropertyObj.WATCH_ID, "")
                                for (temp: ResDeviceConfig in it2) {
                                    if ("WTH" == temp.deviceType) {
                                        DBManager.withDB().withProperty().setProperty(PropertyObj.WATCH_ID, temp.deviceId)
                                    }
                                    DBManager.withDB().withDeviceConfig().insert(temp)
                                }
                                paramFunc(true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call<ResponseDeviceConfigData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    //설정 가저오기
    fun getCMSConfig(paramFunc: (List<CmsConfig>) -> Unit) {
        ApiService.getCMSConfig(
            RequestUserID(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            )
        ).enqueue(object : Callback<ResponseCmsConfigData> {
            override fun onResponse(call: Call<ResponseCmsConfigData>, response: Response<ResponseCmsConfigData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(emptyList())
                                return
                            }

                            it.datas?.let { it2 ->
                                for (temp: CmsConfig in it2) {
                                    DBManager.withDB().withProperty().setProperty(temp.optCode, temp.optValue)
                                }
                                paramFunc(it2)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call<ResponseCmsConfigData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(emptyList())
            }
        })
    }

    fun getWorkPlan(paramFunc: (Boolean) -> Unit) {
        ApiService.getWorkPlan(
            UserIdRequestData(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                workDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            )
        ).enqueue(object : Callback<ResponseWorkPlanData> {
            override fun onResponse(call: Call<ResponseWorkPlanData>, response: Response<ResponseWorkPlanData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }

                            it.datas?.let { it2 ->

                                DBManager.withDB().withWorkCheckList().deletePlans()
                                DBManager.withDB().withWorkPlan().deletePlans()

                                var checkInsert = false;

                                for (temp: ResWorkPlan in it2) {
                                    if (temp.workStatus != "0") {
                                        DBManager.withDB().withWorkCheckList().deletePlan(temp.workId)
                                        DBManager.withDB().withWorkPlan().deletePlan(temp.workId)

                                        if (temp.workStatus != "3" && !checkInsert) {
                                            DBManager.withDB().withWorkCheckList().insert(temp)
                                            checkInsert = true
                                        }
                                    }
                                    DBManager.withDB().withWorkPlan().insert(temp)
                                }

                                if (!checkInsert) {
                                    val plan = DBManager.withDB().withWorkPlan().getTodayPlan()
                                    if (plan != null) {
                                        DBManager.withDB().withWorkCheckList().insert(
                                            ResWorkPlan(
                                                workId = plan.workId,
                                                workDate = plan.workDate,
                                                workPlace = plan.workPlace,
                                                workRole = plan.workRole,
                                                startTime = plan.startTime,
                                                endTime = plan.endTime,
                                                workStatus = plan.workStatus,
                                                workStatusText = plan.workStatusText,
                                                aps = ArrayList()
                                            )
                                        )
                                    }
                                }

                                paramFunc(true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call<ResponseWorkPlanData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    fun sendAllBioLog() {
        if (DBManager.withDB().withProperty().getProperty(PropertyObj.RETRY_SEND_ERR) == "true") {
            sendBioLog(DBManager.withDB().withBioLog().selectLogBlp()) { blp ->
                if (blp) {
                    sendBioLog(DBManager.withDB().withBioLog().selectLogEcg()) { ecg ->
                        if (ecg) {
                            sendBioLog(DBManager.withDB().withBioLog().selectLogGps()) { gps ->
                                if (gps) {
                                    sendBioLog(DBManager.withDB().withBioLog().selectLogHtr()) { htr ->
                                        if (htr) {
                                            sendBioLog(DBManager.withDB().withBioLog().selectLogOxs()) { oxs ->
                                                if (oxs) {
                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogBls()) { bls ->
                                                        if (bls) {
                                                            sendBioLog(DBManager.withDB().withBioLog().selectLogBdy()) { bdy ->
                                                                if (bdy) {
                                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogTmm()) { tmm ->
                                                                        if (tmm) {
                                                                            sendBioLog(DBManager.withDB().withBioLog().selectLogSleepSesstion()) { ses ->
                                                                                if (ses) {
                                                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogSleepStage()) { slp ->
                                                                                        if (slp) {
                                                                                            sendBioLog(DBManager.withDB().withBioLog().selectLogBas()) { bas ->
                                                                                                if (bas) {
                                                                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogActivity()) { act ->
                                                                                                        if (act) {
                                                                                                            sendBioLog(DBManager.withDB().withBioLog().selectLogCalorie()) { cal ->
                                                                                                                if (cal) {
                                                                                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogDistance()) { dis ->
                                                                                                                        if (dis) {
                                                                                                                            sendBioLog(DBManager.withDB().withBioLog().selectLogStep()) { stp ->
                                                                                                                                if (stp) {
                                                                                                                                    sendBioLog(DBManager.withDB().withBioLog().selectLogCho()) {}
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun sendBioLog(req: RequestBioLog) {
        sendBioLog(req) {
            if (req.topic == "bio_gyr" || req.topic == "bio_acc") return@sendBioLog
            if (it) {
                sendAllBioLog()
            }
        }

    }

    private fun sendBioLog(req: RequestBioLog, paramFunc: (Boolean) -> Unit) {
        if (req.datas.isEmpty()) {
            paramFunc(true)
            return
        }
        ApiService.sendBioLog(req).enqueue(object : Callback<ResponseBioLog> {
            override fun onResponse(call: Call<ResponseBioLog>, response: Response<ResponseBioLog>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }
                            DBManager.withDB().withBioLog().updateLog(req)
                            paramFunc(true)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call<ResponseBioLog>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    fun sendUnauthorizedLeave(req: RequestUnauthorizedLeave, paramFunc: (Boolean) -> Unit) {
        ApiService.sendUnauthorizedLeave(
            req
        ).enqueue(object : Callback<ResponseUnauthorizedLeave> {
            override fun onResponse(call: Call<ResponseUnauthorizedLeave>, response: Response<ResponseUnauthorizedLeave>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")

                    if (response.code() == 200) {
                        val rsb = response.body();
                        rsb?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(true)
                                return
                            }
                            paramFunc(true)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call<ResponseUnauthorizedLeave>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }

        })

    }

    fun getBioSync(paramFunc: (Boolean) -> Unit) {

        var dayOfWeek: Int = CommonUtil.getDayOfWeek(LocalDate.now().toString())

        ApiService.getBioSync(
            RequestBioSyncData(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                period = if (DBManager.withDB().withProperty().getProperty(PropertyObj.SYNC_BIO_SET) == "") "7" else if (dayOfWeek == 2) "5" else "3"
            )
        ).enqueue(object : Callback<ResponseBioSyncData> {
            override fun onResponse(call: Call<ResponseBioSyncData>, response: Response<ResponseBioSyncData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }

                            it.datas?.let { it2 ->
                                for (temp: BioSyncData in it2) {
                                    when {
                                        temp.data_type == "PROFILE" -> profileSync(temp)
                                        temp.data_type == "BDY" -> bioBdySync(temp)
                                        temp.data_type == "BLP" -> bioBlpSync(temp)
                                        temp.data_type == "BLS" -> bioBlsSync(temp)
                                        temp.data_type == "ECG" -> bioEcgSync(temp)
                                        temp.data_type == "BAS" -> bioBasSync(temp)
                                        temp.data_type == "HTR" -> bioHtrSync(temp)
                                        temp.data_type == "OXS" -> bioOxsSync(temp)
                                        temp.data_type == "TMM" -> bioTmmSync(temp)
                                        temp.data_type == "CHO" -> bioChoSync(temp)
                                        temp.data_type == "SLEEP_SESSION" -> bioSleepSessionSync(temp)
                                        temp.data_type == "ACTIVITY" -> bioActivitySync(temp)
                                        temp.data_type == "CALORIE" -> bioCalorieSync(temp)
                                        temp.data_type == "DISTANCE" -> bioDistanceSync(temp)
                                        temp.data_type == "STEP" -> bioStepSync(temp)

                                    }
                                }
                            }

                            DBManager.withDB().withProperty().setProperty(PropertyObj.SYNC_BIO_SET, "Y")
                            paramFunc(true)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call<ResponseBioSyncData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    private fun profileSync(data: BioSyncData) {
        data.etc1?.let { DBManager.withDB().withProperty().setProperty(PropertyObj.GENDER, it) }
        data.value1?.let { DBManager.withDB().withProperty().setProperty(PropertyObj.AGE, toIntStringData(it)) }
        data.value2?.let { DBManager.withDB().withProperty().setProperty(PropertyObj.HEIGHT, toDoubleStringData(it)) }
        data.value3?.let { DBManager.withDB().withProperty().setProperty(PropertyObj.WEIGHT, toDoubleStringData(it)) }
    }

    private fun bioActivitySync(data: BioSyncData) {
        DBManager.withDB().withBioActivity().insertOrReplace(
            BioActivity(
                GET_TIME = data.get_time!!,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                START_DATE = data.date1,
                END_DATE = data.date2,
                PERIOD_MINUTE = toDoubleStringData(data.value1!!),   // 실수로(소수점 2자리 반올림)
                TYPE = data.etc1,
                TYPE_TEXT = data.etc2,
                SOURCE = data.etc3,
                SOURCE_TEXT = data.etc4,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioBasSync(data: BioSyncData) {

        if (data.device_id == "") data.device_id = "9fa242e9-b9cc-4674-ac22-2b72a91A2660"

        DBManager.withDB().withBioBas().insertOrReplace(
            BioBas(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                DATA_TYPE = data.etc1,
                BPM = toIntStringData(data.value1!!),   // 심박수는 정수 처리
                REQULARITY = toIntStringData(data.value2!!), // 규칙도는 정수 처리
                FTPPATH = data.etc2,
                POSITION = data.etc3,
                DIAGNOSIS = data.etc4,
                STATUS = data.etc5,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
        DBManager.withDB().withWorkCheckList().updateBioCheck("BAS", data.get_time!!)
    }

    private fun bioBdySync(data: BioSyncData) {
        DBManager.withDB().withBioBdy().insertOrReplace(
            BioBdy(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                HT = toDoubleStringData(data.value1!!), // 신장은 실수 처리(소수점 2자리 반올림)
                WT = toDoubleStringData(data.value2!!), // 체중은 실수 처리(소수점 2자리 반올림)
                BFM = toDoubleStringData(data.value3!!), // 체지방량은 실수 처리(소수점 2자리 반올림)
                BMI = toDoubleStringData(data.value4!!), // 신체질량지수는 실수 처리(소수점 2자리 반올림)
                BMR = toDoubleStringData(data.value5!!), // 기초대사량는 실수 처리(소수점 2자리 반올림)
                PBF = toDoubleStringData(data.value6!!), // 체지방률은 실수 처리(소수점 2자리 반올림)
                SMM = toDoubleStringData(data.value7!!), // 골격근량은 실수 처리(소수점 2자리 반올림)
                TBW = toDoubleStringData(data.value8!!), // 체수분량은 실수 처리(소수점 2자리 반올림)
                WHR = "",   // 복부지량률은 실수 처리(소수점 2자리 반올림)
                GENDER = data.etc1,
                VFL = "",
                WT_MIN = "",
                WT_MAX = "",
                SMM_MIN = "",
                SMM_MAX = "",
                BFM_MIN = "",
                BFM_MAX = "",
                VFL_MIN = "",
                VFL_MAX = "",
                IBMI = "",
                BMI_MIN = "",
                BMI_MAX = "",
                IPBF = "",
                PBF_MIN = "",
                PBF_MAX = "",
                IWHR = "",
                WHR_MIN = "",
                WHR_MAX = "",
                BMR_MIN = "",
                BMR_MAX = "",
                WC = "",
                MC = "",
                FC = "",
                FS = "",
                IBFM = "",
                IFFM = "",
                MAX_WT = "",
                MIN_WT = "",
                MAX_SMM = "",
                MIN_SMM = "",
                MAX_BFM = "",
                MIN_BFM = "",
                MAX_BMI = "",
                MIN_BMI = "",
                MAX_PBF = "",
                MIN_PBF = "",
                DESCRIPTION = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y"
            )
        )
    }

    private fun bioBlpSync(data: BioSyncData) {
        DBManager.withDB().withBioBlp().insertOrReplace(
            BioBlp(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                SYSTOLIC = toIntStringData(data.value1!!), // 혈압은 정수로
                DIASTOLIC = toIntStringData(data.value2!!),   // 혈압은 정수로
                HR = toIntStringData(data.value3!!), // 심박수는 정수로
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y"
            )
        )
    }

    private fun bioBlsSync(data: BioSyncData) {
        DBManager.withDB().withBioBls().insertOrReplace(
            BioBls(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                BLS_VALUE = toIntStringData(data.value1!!),   // 혈당은 정수로
                TAG1 = data.etc1!!,
                TAG2 = toIntStringData(data.value2!!),    // 식사량은 정수로
                TAG3 = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y"
            )
        )
    }

    private fun bioCalorieSync(data: BioSyncData) {
        DBManager.withDB().withBioCalorie().insertOrReplace(
            BioCalorie(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                START_DATE = data.date1,
                END_DATE = data.date2,
                ENERGY_KCAL = toDoubleStringData(data.value1!!),   // 실수로(소수점 2자리 반올림)
                SOURCE = data.etc1,
                SOURCE_TEXT = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioChoSync(data: BioSyncData) {
        DBManager.withDB().withBioCho().insertOrReplace(
            BioCho(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                CHO_TYPE = data.etc1!!,
                CHO_VALUE = toIntStringData(data.value1!!),   // 콜레스테롤은 정수로
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioDistanceSync(data: BioSyncData) {
        DBManager.withDB().withBioDistance().insertOrReplace(
            BioDistance(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                START_DATE = data.date1,
                END_DATE = data.date2,
                DISTANCE_METER = toDoubleStringData(data.value1!!),   // 실수로(소수점 2자리 반올림)
                SOURCE = data.etc1,
                SOURCE_TEXT = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioHtrSync(data: BioSyncData) {
        DBManager.withDB().withBioHtr().insertOrReplace(
            BioHtr(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                HEART_RATE = toIntStringData(data.value1!!), // 심박수는 정수로
                TAG1 = data.etc1,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y"
            )
        )
    }

    private fun bioOxsSync(data: BioSyncData) {
        DBManager.withDB().withBioOxs().insertOrReplace(
            BioOxs(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                SPO2 = toIntStringData(data.value1!!), // 산소포화도는 정수로
                HEART_RATE = toIntStringData(data.value2!!), // 심박수는 정수로
                TAG1 = data.etc1,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioSleepSessionSync(data: BioSyncData) {
        DBManager.withDB().withBioSleepSession().insertOrReplace(
            BioSleepSession(
                SESSION_ID = data.etc1!!.toLong(),
                GET_TIME = data.get_time,
                DEVICE_ID = data.device_id,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                SESSION_START_DATE = data.date1,
                SESSION_END_DATE = data.date2,
                SESSION_PERIOD = toIntStringData(data.value1!!).toLong(),
                SESSION_DEEP = toIntStringData(data.value2!!).toLong(),
                SESSION_LIGHT = toIntStringData(data.value3!!).toLong(),
                SESSION_REM = toIntStringData(data.value4!!).toLong(),
                SESSION_ETC = toIntStringData(data.value5!!).toLong(),
                SESSION_SOURCE = data.etc1,
                SESSION_SOURCE_TEXT = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioStepSync(data: BioSyncData) {
        DBManager.withDB().withBioStep().insertOrReplace(
            BioStep(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                START_DATE = data.date1,
                END_DATE = data.date2,
                COUNT = toIntStringData(data.value1!!),   // 정수로
                SOURCE = data.etc1,
                SOURCE_TEXT = data.etc2,
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioTmmSync(data: BioSyncData) {
        DBManager.withDB().withBioTmm().insertOrReplace(
            BioTmm(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                TEMPERATURE = toDoubleStringData(data.value1!!),   // 체온은 실수로(소수점 2자리 반올림)
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

    private fun bioEcgSync(data: BioSyncData) {
        DBManager.withDB().withBioEcgHeader().insertOrReplace(
            BioEcgHeader(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                CREATE_DTTM = data.get_time,
            )
        )
        DBManager.withDB().withBioEcg().insertOrReplace(
            BioEcg(
                GET_TIME = data.get_time!!,
                DEVICE_ID = data.device_id!!,
                DEVICE_SERIAL = data.device_serial,
                ECG_INDEX = toIntStringData(data.value1!!),
                ECG_DATA = data.etc1,
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )
    }

}