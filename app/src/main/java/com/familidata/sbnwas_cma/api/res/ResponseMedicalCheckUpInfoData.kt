package com.familidata.sbnwas_cma.api.res

data class ResponseMedicalCheckUpInfoData(
    var error: Error?, var datas: List<MedicalInfoData>?
)

data class MedicalInfoData(
    var title: String,
    var value_0: String,
    var value_1: String,
    var value_2: String,
    var link: String,
)
