package com.familidata.sbnwas_cma.api.res

data class ResponseDeviceConfigData(
    var error: Error?, var datas: List<ResDeviceConfig>?
)

data class ResDeviceConfig(
    val deviceId: String,
    val deviceOrder: String,
    val deviceType: String,
    val deviceTypeName: String?,
    val deviceName: String?,
    val deviceModel: String?,
    val deviceVersion: String?,
    val deviceCompany: String?,
    val appPackage: String?,
    val appActivity: String?,
    val appIcon: String?
)
