package com.familidata.sbnwas_cma.common.job.inwork

import android.content.Context
import android.net.wifi.WifiManager
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestUnauthorizedLeave
import com.familidata.sbnwas_cma.api.req.UnAuthorizedLeave
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


//근무지이탈여부 확인
class CheckOnWorkExecutor : IExecutor {
    override suspend fun execute(context: Context) {
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.InWork.WI_RETRY_MIN_WAP)) {
            return
        }
        if (!DBManager.withDB().withWorkPlan().isOnWork()) {  //일하는중인지 확인
            return
        }

        Log.i("근무지 이탈여부 확인 START")

        if (!DBManager.withDB().withAp().isOnWOrk(getAPBssid(context))) {
            CommonUtil.createNotificationChannel(
                context, context.getString(R.string.msg_warning_out_of_area),
                context.getString(R.string.msg_warning_out_of_area_desc)
            )

            val onWork: WorkPlan? = DBManager.withDB().withWorkPlan().getInWork(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )

            onWork?.let {

                val datas: MutableList<Any> = ArrayList()
                datas.add(
                    UnAuthorizedLeave(
                        leave_check_time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                        work_id = it.workId,
                        device_id = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)
                    )
                )

                CoroutineScope(Dispatchers.Main).launch {
                    CommonApi().sendUnauthorizedLeave(RequestUnauthorizedLeave("unauthorized_leave", datas), {})
                }
            }

        }
    }

    fun getAPBssid(context: Context): String {
        var bssid = ""
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            Log.i("bssid", info.bssid)
            bssid = info.bssid
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return bssid
    }
}