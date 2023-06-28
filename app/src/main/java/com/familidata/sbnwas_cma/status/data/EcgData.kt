package com.familidata.sbnwas_cma.status.data

data class EcgData(
    val memb_id : String,
    val ecg : Int,
    val ppg_green : Int,
    val sequence : Int,
    val get_no : Int,
    val max_threshold : Int,
    val min_threshold : Int,
    val get_time : String,
)
