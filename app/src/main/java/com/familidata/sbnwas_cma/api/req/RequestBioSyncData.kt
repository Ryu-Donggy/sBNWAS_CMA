package com.familidata.sbnwas_cma.api.req

import com.google.gson.annotations.SerializedName

data class RequestBioSyncData(
    val userId: String,
    val period: String
)
