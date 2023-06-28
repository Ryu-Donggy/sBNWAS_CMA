package com.familidata.sbnwas_cma.api.res

data class ResponseBioAnalyzeData(
    var error: Error?, var data: AnalyzeData?
)

data class AnalyzeData(
    var create_dttm: String,
    var memb_id: String,
    var question: String,
    var answer: String?,
    var analysis_type: String?,
    var create_date: String?,
    var analysis_id: String,
)
