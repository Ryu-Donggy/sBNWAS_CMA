package com.familidata.sbnwas_cma.api.res

data class ResponseCmsConfigData(
    var error: Error?, var datas: List<CmsConfig>?
)


data class CmsConfig(
    val optType: String?,
    val optSource: String?,
    val optName: String?,
    val optCode: String,
    val optValue: String,
    val valueType: String?,
    val optPeriodType: String?,
)
