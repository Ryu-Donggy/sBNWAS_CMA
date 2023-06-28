package com.familidata.sbnwas_cma.api.res

data class ResponseWorkPlanData(
    var error: Error?, var datas: List<ResWorkPlan>?
)

data class ResWorkPlan(
    var workId: String,
    var workDate: String,
    var workPlace: String,
    var workRole: String,
    var startTime: String,
    var endTime: String,
    var workStatus: String,
    var workStatusText: String,
    var aps: List<ResAp>,
)


data class ResAp(
    var apId: String,
    var imei: String,
    var accessId: String,
    var macAddress: String,
)