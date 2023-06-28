package com.familidata.sbnwas_cma.api.req

import com.google.gson.annotations.SerializedName

/**
 * <pre>
 * 근무지 이탈
 * </pre>
 *
 * leave_check_time : 이탈 확인 시간
 * memb_id: 사용자 UID
 * work_id: 근무 UID
 * device_id: 이탈 확인 장비
 */
data class RequestUnauthorizedLeave(
    var topic: String,
    var datas: List<Any>
)

data class UnAuthorizedLeave(
    val leave_check_time: String?,
    @SerializedName("memb_id")  var userId: String,
    val work_id: String?,
    val device_id: String?,
)
