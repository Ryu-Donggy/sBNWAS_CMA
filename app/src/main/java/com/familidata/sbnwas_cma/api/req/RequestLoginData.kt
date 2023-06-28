package com.familidata.sbnwas_cma.api.req

import com.google.gson.annotations.SerializedName

data class RequestLoginData(
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("password")
    val password: String


)