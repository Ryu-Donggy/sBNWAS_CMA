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
class DeviceListReflashExecutor : IExecutor {
    override suspend fun execute(context: Context) {
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.SYNC_DEVICE_SET)) {
            return
        }
        Log.i("장비 리스트 갱신 집행자  START")
        try {
            CoroutineScope(Dispatchers.Main).launch {
                CommonApi().getDeviceList {}
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d(e.printStackTrace())

        }
    }
}