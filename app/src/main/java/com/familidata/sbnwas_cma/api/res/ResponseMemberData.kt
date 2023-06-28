package com.familidata.sbnwas_cma.api.res

data class ResponseMemberData(
    var error: Error?, var datas: List<ResMember>?
)

data class ResMember(
    var loginId: String,
    var userId: String,
    var userName: String,
    val telNo: String,
    val age: String,
    val gender: String,
    val height: String,
    val weight: String,
)