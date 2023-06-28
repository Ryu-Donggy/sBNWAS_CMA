package com.familidata.sbnwas_cma.common.job

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.common.job.inwork.CheckOnWorkExecutor
import com.familidata.sbnwas_cma.common.job.inwork.InWorkBioCheckExecutor
import com.familidata.sbnwas_cma.common.job.inwork.InWorkWatchCheckExecutor
import com.familidata.sbnwas_cma.common.job.prework.PreWorkBioCheckExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj

class JobInWork : IExecutor {
    override suspend fun execute(context: Context) {

        if (!DBManager.withDB().withWorkPlan().isOnWork()) {
            return
        }

        // Watch 상태 check (15분 주기로 반드시 실행)
        InWorkWatchCheckExecutor().execute(context = context)

        // 근무지 이탈 확인 (15분 주기로 반드시 실행)
        CheckOnWorkExecutor().execute(context = context)

        // 건강 정보 확인 (CMA 주기 설정에 따름)
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.InWork.WI_RETRY_MIN_ALARM) ||
            !DBManager.withDB().withWorkPlan().isOnWork()) {
            return
        }

        InWorkBioCheckExecutor().execute(context = context)
    }
}