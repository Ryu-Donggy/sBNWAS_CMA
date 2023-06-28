package com.familidata.sbnwas_cma.api.res

import com.google.gson.annotations.SerializedName

data class ResponseUserInfoData(
    var error: Error?, var data: UserInfoDetail?

)

data class UserInfoDetail(
    var loginId: String? = null,
    var userName: String? = null,
    var gender: String? = null,
    var birth: String? = null,
    var telNo: String? = null,
    var zipCode: String? = null,
    var address: String? = null,
    var joinDate: String? = null,
    var empId: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var age: String? = null,
)