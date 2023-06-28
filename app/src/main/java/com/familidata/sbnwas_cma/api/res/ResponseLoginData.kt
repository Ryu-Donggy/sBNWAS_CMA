package com.familidata.sbnwas_cma.api.res

data class ResponseLoginData(
    var error: Error?,
    var data: LoginResult?
)

data class LoginResult(
    var userId: String = "",
    var userName: String = "",
    var loginId: String = "",
    var age: String = "",
    var gender: String = "",
    var height: String = "",
    var weight: String= "",
)


