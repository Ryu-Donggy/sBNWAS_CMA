package com.familidata.sbnwas_cma.common.job.inwork

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.main.workstatus.WatchConnector
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.util.CommonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.concurrent.Executors

class InWorkWatchCheckExecutor : IExecutor {

    companion object {
        var SEND_RESULT: Int = -1
    }

    private var context: Context? = null
    val executor = Executors.newFixedThreadPool(1)


    override suspend fun execute(context: Context) {

        Log.i("InWorkWatchCheckExecutor START")
        this.context = context

        val json = JSONObject()
        json.put("GET_STATUS", "Y")
        executor.execute(WatchConnector(context = context, json, executor).checkConnection())

        autoCloseProgress(context)

    }


    private fun autoCloseProgress(context: Context) {
        GlobalScope.launch {
            launch(Dispatchers.Main) {
                delay(10000)
                if (SEND_RESULT == -1) {
                    // Watch 정보 미 수신
                    Log.i("Watch 정보 미수신")
                    CommonUtil.createNotificationChannel(
                        context, context.getString(R.string.noti_watch_info_non_accepted), context.getString(R.string.noti_watch_info_non_accepted_desc)
                    )
                } else if (SEND_RESULT == 0) {
                    Log.i("Watch 정보 미 동기화 수신")
                    // Watch 설정 미 동기화
                    val json = JSONObject()
                    json.put(PropertyObj.InWork.WI_BIO_GYR, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GYR))
                    json.put(PropertyObj.InWork.WI_BIO_ACC, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_ACC))
                    json.put(PropertyObj.InWork.WI_BIO_GPS, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GPS))
                    executor.execute(WatchConnector(context = context, json, executor).checkConnection())
                } else {
                    // Watch 정보 수신
                    Log.i("Watch 정보 수신")
                    SEND_RESULT = -1
                }
            }

        }
    }

}