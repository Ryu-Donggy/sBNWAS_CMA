package com.familidata.sbnwas_cma.status.data

data class GpsData(
    val memb_id : String,
    val longitude : Double,
    val latitude : Double,
    val altitude : Double,
    val get_time : String,
)
