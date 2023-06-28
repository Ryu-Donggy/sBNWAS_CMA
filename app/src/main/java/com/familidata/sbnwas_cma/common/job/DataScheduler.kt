package com.familidata.sbnwas_cma.common.job

import android.content.Context
import androidx.work.WorkerParameters
import com.familidata.sbnwas_cma.common.PDataScheduler
import com.familidata.sbnwas_cma.common.job.common.BlpExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj


class DataScheduler(context: Context, params: WorkerParameters) : PDataScheduler(context, params) {
    override suspend fun doWork(): Result {
        super.doWork()

        if (DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID) == "") {
            return Result.success()
        }

        JobCommon().execute(applicationContext)
        JobPreWork().execute(applicationContext)
        JobInWork().execute(applicationContext)
        return Result.success()
    }
}