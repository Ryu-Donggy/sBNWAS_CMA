package com.familidata.sbnwas_cma.api.req

import com.google.gson.annotations.SerializedName

data class RequestSleepStageSyncData(
    val userId: String,
    val sessionId: Long
)
