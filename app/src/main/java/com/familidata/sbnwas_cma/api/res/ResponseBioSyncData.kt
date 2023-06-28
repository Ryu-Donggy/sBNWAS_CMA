package com.familidata.sbnwas_cma.api.res

import com.google.gson.annotations.SerializedName

data class ResponseBioSyncData(
    var error: Error?,
    var datas : List<BioSyncData>?
)

data class BioSyncData(
    var data_type: String,
    var get_time: String?,
    var device_id: String?,
    var device_serial: String?,
    var date1: String?,
    var date2: String?,
    var date3: String?,
    var date4: String?,
    var date5: String?,
    var value1: String?,
    var value2: String?,
    var value3: String?,
    var value4: String?,
    var value5: String?,
    var value6: String?,
    var value7: String?,
    var value8: String?,
    var value9: String?,
    var value10: String?,
    var etc1: String?,
    var etc2: String?,
    var etc3: String?,
    var etc4: String?,
    var etc5: String?,
    var etc6: String?,
    var etc7: String?,
    var etc8: String?,
    var etc9: String?,
    var etc10: String?,
)