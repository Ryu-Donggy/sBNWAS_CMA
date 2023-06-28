package com.familidata.sbnwas_cma.common.job.prework

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.main.workstatus.WatchConnector
import com.familidata.sbnwas_cma.util.CommonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.concurrent.Executors

class PreWorkWatchCheckExecutor : IExecutor {

    companion object {
        var SEND_RESULT: Int = -1
    }

    private var context: Context? = null
    val executor = Executors.newFixedThreadPool(1)

    override suspend fun execute(context: Context) {

        Log.i("PreWorkWatchCheckExecutor START")
        this.context = context

        val json = JSONObject()
        json.put("GET_STATUS", "Y")
        executor.execute(WatchConnector(context = context, json, executor).checkConnection())

        autoCloseProgress(context);

    }


    private fun autoCloseProgress(context: Context) {
        GlobalScope.launch {
            launch(Dispatchers.Main) {
                delay(10000)
                if (SEND_RESULT == -1) {
                    // Watch 정보 미 수신
                    Log.i("Watch 정보 미 수신")
                    CommonUtil.createNotificationChannel(context, context.getString(R.string.noti_watch_info_non_accepted), context.getString(R.string.noti_watch_info_non_accepted_desc))
                } else {
                    // Watch 정보 수신
                    Log.i("Watch 정보 수신")
                    PreWorkWatchCheckExecutor.SEND_RESULT = -1;
                }
            }

        }
    }

}