package com.familidata.sbnwas_cma.common.job.common

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//CMA 시스템 설정 동기화
class ConfigReflashExecutor : IExecutor {
    override suspend fun execute(context: Context) {
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.SYNC_CMA_SET)) {
            return
        }
        Log.i("설정 집행자  START")
        try {
            CoroutineScope(Dispatchers.Main).launch {
                CommonApi().getCMSConfig {
                    if (it.isEmpty()) {

                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d(e.printStackTrace())
        }
    }
}