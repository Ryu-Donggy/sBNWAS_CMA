package com.familidata.sbnwas_cma.api.req

import androidx.health.connect.client.records.BasalBodyTemperature
import com.google.gson.annotations.SerializedName
import java.util.Objects

data class RequestBioLog(
    var topic: String, var datas: List<Any>
)

data class BlpBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var systolic: String?,
    var diastolic: String?,
    var hr: String?,
    var device_serial: String?,
)

data class EcgBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var ecg_index: String,
    var ecg_data: String?,
    var device_serial: String?,
    var create_dttm: String?,
)

data class GpsBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var longitude: String?,
    var latitude: String?,
    var altitude: String?,
    var device_serial: String?,
)


data class GyrBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var axisx: String?,
    var axisy: String?,
    var axisz: String?,
    var device_serial: String?,
)

data class HsmBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var bpm: String?,
    var reqularity: String?,
    var db: String?,
    var ftppath: String?,
    var skeeper_id: String?,
    var position: String?,
    var device_serial: String?,
)

data class LsmBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var bpm: String?,
    var reqularity: String?,
    var db: String?,
    var ftppath: String?,
    var skeeper_id: String?,
    var position: String?,
    var device_serial: String?,
)

data class HtrBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var heart_rate: String?,
    var device_serial: String?,
)


data class OxsBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var spo2: String?,
    var heart_rate: String?,
    var device_serial: String?,
)


data class TmmBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var temperature: String?,
    var device_serial: String?,
)

data class AccBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var axisx: String?,
    var axisy: String?,
    var axisz: String?,
    var device_serial: String?,
)

data class BlsBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var bls_value: String?,
    var tag1: String?,
    var tag2: String?,
    var tag3: String?,
    var device_serial: String?,
)


data class BdyBioData(
    var get_time: String,
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var ht: String?,
    var wt: String?,
    var bfm: String?,
    var smm: String?,
    var pbf: String?,
    var bmi: String?,
    var whr: String?,
    var gender: String?,
    var vfl: String?,
    var wt_min: String?,
    var wt_max: String?,
    var smm_min: String?,
    var smm_max: String?,
    var bfm_min: String?,
    var bfm_max: String?,
    var vfl_min: String?,
    var vfl_max: String?,
    var ibmi: String?,
    var bmi_min: String?,
    var bmi_max: String?,
    var ipbf: String?,
    var pbf_min: String?,
    var pbf_max: String?,
    var iwhr: String?,
    var whr_min: String?,
    var whr_max: String?,
    var bmr: String?,
    var bmr_min: String?,
    var bmr_max: String?,
    var wc: String?,
    var mc: String?,
    var fc: String?,
    var fs: String?,
    var ibfm: String?,
    var iffm: String?,
    var max_wt: String?,
    var min_wt: String?,
    var max_smm: String?,
    var min_smm: String?,
    var max_bfm: String?,
    var min_bfm: String?,
    var max_bmi: String?,
    var min_bmi: String?,
    var max_pbf: String?,
    var min_pbf: String?,
    var tbw: String?,
    var description: String?,
    var device_serial: String?,
)

data class SleepSessionBioData(
    var session_id: Long,
    var device_id: String,
    @SerializedName("memb_id") var userId: String,
    var get_time: String,
    var session_start_date: String?,
    var session_end_date: String?,
    var session_period: Long?,
    var session_deep: Long?,
    var session_light: Long?,
    var session_rem: Long?,
    var session_etc: Long?,
    var session_source: String?,
    var session_source_text: String?,
    var device_serial: String?,
)

data class SleepStageBioData(
    var stage_id: Long,
    var session_id: Long,
    @SerializedName("memb_id") var userId: String,
    var stage_code: String,
    var stage_text: String?,
    var stage_start_date: String?,
    var stage_end_date: String?,
    var stage_period: Long?,
)

data class ActionPatternData(
    @SerializedName("memb_id") var userId: String,
    var start: String?,
    var end: String?,
    var type: String?,
    var sensor: String?
)


data class BasBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var data_type: String?,
    var bpm: String?,
    var reqularity: String?,
    var ftppath: String?,
    var position: String?,
    var device_serial: String?,
    var diagnosis: String?,
    var status: String?,
)

data class ActivityBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var device_serial: String?,
    var start_date: String?,
    var end_date: String?,
    var period_minute: String,
    var type: String?,
    var type_text: String?,
    var source: String?,
    var source_text: String?
)

data class CalorieBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var device_serial: String?,
    var start_date: String?,
    var end_date: String?,
    var energy_kcal: String,
    var source: String?,
    var source_text: String?
)

data class DistanceBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var device_serial: String?,
    var start_date: String?,
    var end_date: String?,
    var distance_meter: String,
    var source: String?,
    var source_text: String?
)

data class StepBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var device_serial: String?,
    var start_date: String?,
    var end_date: String?,
    var count: String,
    var source: String?,
    var source_text: String?
)

data class ChoBioData(
    @SerializedName("memb_id") var userId: String,
    var device_id: String,
    var get_time: String,
    var cho_type: String?,
    var cho_value: String?,
    var device_serial: String?,
)

data class BioTag(
    @SerializedName("memb_id") var userId: String,
    var get_time: String,
    var bio_type: String?,
    var tag1: String?,
    var tag2: String?,
    var tag3: String?,
    var tag4: String?,
    var tag5: String?,
)
