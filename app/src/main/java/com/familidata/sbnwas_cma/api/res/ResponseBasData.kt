package com.familidata.sbnwas_cma.api.res

data class ResponseXsmData(
    var error: Error?, var data: BasData?
)

data class BasData(
    val reqularity: String = "0",
    val getTime: String = "",
    val todayCount: String = "",
    val ftppath: String = "",
    val position: String = "",
    val bpm: String = "0",
    val userId: String = "",
    val deviceId: String = "",
    val deviceSerial: String = "",
    val diagnosis: String = "",
    val tag1: String = "",
)