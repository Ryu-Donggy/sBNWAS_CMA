package com.familidata.sbnwas_cma.api.res

data class ResponseWorkStatusData(
    var error: Error?, var data: ResWOrkStatus?
)

data class ResWOrkStatus(
    var workStatus: String,
    var workStatusText: String,
)
