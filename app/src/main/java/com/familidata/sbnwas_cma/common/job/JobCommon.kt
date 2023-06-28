package com.familidata.sbnwas_cma.common.job

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.common.job.common.*
import com.familidata.sbnwas_cma.common.job.inwork.CheckOnWorkExecutor

class JobCommon : IExecutor {
    override suspend fun execute(context: Context) {
        Log.i("JobCommon START")
        BlpExecutor().execute(context = context)
        ConfigReflashExecutor().execute(context = context)
        DeviceListReflashExecutor().execute(context = context)
        WorkPlanReflashExecutor().execute(context = context)
        HealthConnectorExecutor().execute(context = context)
    }
}