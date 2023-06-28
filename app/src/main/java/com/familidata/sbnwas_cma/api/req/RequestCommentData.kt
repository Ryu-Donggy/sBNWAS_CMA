package com.familidata.sbnwas_cma.api.req


//"{
//userId : 사용자 UID
//workId : 근무 UID
//comments : 특이사항
//}"
data class RequestCommentData(
    var userId: String,
    var workId: String?,
    var comments: String? = null,
)