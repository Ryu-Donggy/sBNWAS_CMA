package com.familidata.sbnwas_cma.common

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.familidata.sbnwas_cma.room.DBManager


open class PDataScheduler(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        DBManager.withDB().withProperty().setSchedulerCnt()
        Log.e("======================================>", "WorkManager 스케줄러 루틴 시작")
        return Result.success()
    }
}