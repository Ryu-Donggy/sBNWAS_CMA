package com.familidata.sbnwas_cma.common.job

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.common.job.prework.PreWorkBioCheckExecutor
import com.familidata.sbnwas_cma.common.job.prework.PreWorkWatchCheckExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj

class JobPreWork : IExecutor {
    override suspend fun execute(context: Context) {

        if (!DBManager.withDB().withWorkPlan().isOnPreWork()) {
            return
        }

        // Watch 상태 check (15분 주기로 반드시 실행)
        PreWorkWatchCheckExecutor().execute(context = context)

        // 건강 정보 확인 (CMA 주기 설정에 따름)
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.PreWork.WB_RETRY_MIN_ALARM)) {
            return
        }

        PreWorkBioCheckExecutor().execute(context = context)

    }

}