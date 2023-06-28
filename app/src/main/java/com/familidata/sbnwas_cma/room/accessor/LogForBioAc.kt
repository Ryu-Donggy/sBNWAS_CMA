package com.familidata.sbnwas_cma.room.accessor

import androidx.room.ColumnInfo
import com.familidata.sbnwas_cma.api.req.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor


class LogForBioAc(db: AppDatabase) : PAccessor(db) {
    private val TAG = LogForBioAc::class.java.name

    fun updateLog(req: RequestBioLog) {
        for (temp in req.datas) {
            when (temp) {
                is BlpBioData -> {
                    db.bioBlpDao().serverSent(temp.get_time, temp.userId)
                }

                is EcgBioData -> {
                    db.bioEcgDao().serverSent(temp.get_time, temp.userId)
                }

                is GpsBioData -> {
                    db.bioGpsDao().serverSent(temp.get_time, temp.userId)
                }

                is HtrBioData -> {
                    db.bioHtrDao().serverSent(temp.get_time, temp.userId)
                }

                is BlsBioData -> {
                    db.bioBlsDao().serverSent(temp.get_time, temp.userId)
                }

                is OxsBioData -> {
                    db.bioOxsDao().serverSent(temp.get_time, temp.userId)
                }

                is BdyBioData -> {
                    db.bioBdyDao().serverSent(temp.get_time, temp.userId)
                }

                is TmmBioData -> {
                    db.bioTmmDao().serverSent(temp.get_time, temp.userId)
                }

                is SleepSessionBioData -> {
                    db.bioSleepSessionDao().serverSent(temp.get_time, temp.userId)
                }

                is SleepStageBioData -> {
                    db.bioSleepStageDao().serverSent(temp.stage_id, temp.session_id)
                }

                is BasBioData -> {
                    db.bioBasDao().serverSent(temp.get_time, temp.userId, temp.device_id)
                }

                is ActivityBioData -> {
                    db.bioActivityDao().serverSent(temp.get_time, temp.userId, temp.device_id)
                }

                is CalorieBioData -> {
                    db.bioCalorieDao().serverSent(temp.get_time, temp.userId, temp.device_id)
                }

                is DistanceBioData -> {
                    db.bioDistanceDao().serverSent(temp.get_time, temp.userId, temp.device_id)
                }

                is StepBioData -> {
                    db.bioStepDao().serverSent(temp.get_time, temp.userId, temp.device_id)
                }

                is ChoBioData -> {
                    db.bioChoDao().serverSent(temp.get_time, temp.userId, temp.device_id, temp.cho_type!!)
                }
            }
        }
    }

    fun selectLogBlp(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioBlpDao().selectForLog()) {
            datas.add(
                BlpBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    systolic = temp.SYSTOLIC,
                    diastolic = temp.DIASTOLIC,
                    hr = temp.HR,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_blp", datas)
    }

    fun selectLogEcg(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioEcgDao().selectForLog()) {
            datas.add(
                EcgBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    create_dttm = temp.CREATE_DTTM,
                    ecg_index = temp.ECG_INDEX,
                    ecg_data = temp.ECG_DATA,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_ecg", datas)
    }

    fun selectLogGps(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioGpsDao().selectForLog()) {
            datas.add(
                GpsBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    longitude = temp.LONGITUDE,
                    latitude = temp.LATITUDE,
                    altitude = temp.ALTITUDE,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_gps", datas)
    }

    fun selectLogHtr(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioHtrDao().selectForLog()) {
            datas.add(
                HtrBioData(
                    get_time = temp.GET_TIME, userId = temp.USER_ID!!, device_id = temp.DEVICE_ID!!, heart_rate = temp.HEART_RATE, device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_htr", datas)
    }

    fun selectLogOxs(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioOxsDao().selectForLog()) {
            datas.add(
                OxsBioData(
                    get_time = temp.GET_TIME, userId = temp.USER_ID!!, device_id = temp.DEVICE_ID!!, heart_rate = temp.HEART_RATE, spo2 = temp.SPO2, device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_oxs", datas)
    }

    fun selectLogBls(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioBlsDao().selectForLog()) {
            datas.add(
                BlsBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    bls_value = temp.BLS_VALUE,
                    tag1 = temp.TAG1,
                    tag2 = temp.TAG2,
                    tag3 = temp.TAG3,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_bls", datas)
    }


    fun selectLogBdy(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioBdyDao().selectForLog()) {
            datas.add(
                BdyBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    ht = temp.HT,
                    wt = temp.WT,
                    bfm = temp.BFM,
                    smm = temp.SMM,
                    pbf = temp.PBF,
                    bmi = temp.BMI,
                    whr = temp.WHR,
                    gender = temp.GENDER,
                    vfl = temp.VFL,
                    wt_min = temp.WT_MIN,
                    wt_max = temp.WT_MAX,
                    smm_min = temp.SMM_MIN,
                    smm_max = temp.SMM_MAX,
                    bfm_min = temp.BFM_MIN,
                    vfl_min = temp.VFL_MIN,
                    bfm_max = temp.BFM_MAX,
                    vfl_max = temp.VFL_MAX,
                    ibmi = temp.IBMI,
                    bmi_min = temp.BMI_MIN,
                    bmi_max = temp.BMI_MAX,
                    ipbf = temp.IPBF,
                    pbf_min = temp.PBF_MIN,
                    pbf_max = temp.PBF_MAX,
                    iwhr = temp.IWHR,
                    whr_min = temp.WHR_MIN,
                    whr_max = temp.WHR_MAX,
                    bmr = temp.BMR,
                    bmr_min = temp.BMR_MIN,
                    bmr_max = temp.BMR_MAX,
                    wc = temp.WC,
                    mc = temp.MC,
                    fc = temp.FC,
                    fs = temp.FS,
                    ibfm = temp.IBFM,
                    iffm = temp.IFFM,
                    max_wt = temp.MAX_WT,
                    min_wt = temp.MIN_WT,
                    max_smm = temp.MAX_SMM,
                    min_smm = temp.MIN_SMM,
                    max_bfm = temp.MAX_BFM,
                    min_bfm = temp.MIN_BFM,
                    max_bmi = temp.MAX_BMI,
                    min_bmi = temp.MIN_BMI,
                    max_pbf = temp.MAX_PBF,
                    min_pbf = temp.MIN_PBF,
                    tbw = temp.TBW,
                    description = temp.DESCRIPTION,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_bdy", datas)
    }

    fun selectLogTmm(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioTmmDao().selectForLog()) {
            datas.add(
                TmmBioData(
                    get_time = temp.GET_TIME, userId = temp.USER_ID!!, device_id = temp.DEVICE_ID!!, temperature = temp.TEMPERATURE, device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_tmm", datas)
    }


    fun selectLogSleepSesstion(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioSleepSessionDao().selectForLog()) {
            datas.add(
                SleepSessionBioData(
                    session_id = temp.SESSION_ID,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    get_time = temp.GET_TIME!!,
                    session_start_date = temp.SESSION_START_DATE!!,
                    session_end_date = temp.SESSION_END_DATE!!,
                    session_period = temp.SESSION_PERIOD!!,
                    session_deep = temp.SESSION_DEEP!!,
                    session_light = temp.SESSION_LIGHT!!,
                    session_rem = temp.SESSION_REM!!,
                    session_etc = temp.SESSION_ETC!!,
                    session_source = temp.SESSION_SOURCE!!,
                    session_source_text = temp.SESSION_SOURCE_TEXT,
                    device_serial = temp.DEVICE_SERIAL,
                )
            )
        }
        return RequestBioLog("bio_sleep_session", datas)
    }

    fun selectLogSleepStage(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioSleepStageDao().selectForLog()) {
            datas.add(
                SleepStageBioData(
                    session_id = temp.SESSION_ID,
                    userId = temp.USER_ID!!,
                    stage_code = temp.STAGE_CODE!!,
                    stage_id = temp.STAGE_ID,
                    stage_end_date = temp.STAGE_END_DATE,
                    stage_period = temp.STAGE_PERIOD,
                    stage_start_date = temp.STAGE_START_DATE,
                    stage_text = temp.STAGE_TEXT
                )
            )
        }
        return RequestBioLog("bio_sleep_stage", datas)
    }

    fun selectLogBas(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioBasDao().selectForLog()) {
            datas.add(
                BasBioData(
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    get_time = temp.GET_TIME!!,
                    data_type = temp.DATA_TYPE!!,
                    bpm = temp.BPM!!,
                    reqularity = temp.REQULARITY!!,
                    ftppath = temp.FTPPATH!!,
                    position = temp.POSITION!!,
                    device_serial = temp.DEVICE_SERIAL!!,
                    diagnosis = temp.DIAGNOSIS!!,
                    status = temp.STATUS!!,
                )
            )
        }
        return RequestBioLog("bio_bas", datas)
    }

    fun selectLogActivity(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioActivityDao().selectForLog()) {
            datas.add(
                ActivityBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    device_serial = temp.DEVICE_SERIAL,
                    start_date = temp.START_DATE,
                    end_date = temp.END_DATE,
                    period_minute = temp.PERIOD_MINUTE!!,
                    type = temp.TYPE,
                    type_text = temp.TYPE_TEXT,
                    source = temp.SOURCE,
                    source_text = temp.SOURCE_TEXT
                )
            )
        }
        return RequestBioLog("bio_activity", datas)
    }

    fun selectLogCalorie(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioCalorieDao().selectForLog()) {
            datas.add(
                CalorieBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    device_serial = temp.DEVICE_SERIAL,
                    start_date = temp.START_DATE,
                    end_date = temp.END_DATE,
                    energy_kcal = temp.ENERGY_KCAL!!,
                    source = temp.SOURCE,
                    source_text = temp.SOURCE_TEXT
                )
            )
        }
        return RequestBioLog("bio_calorie", datas)
    }

    fun selectLogDistance(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioDistanceDao().selectForLog()) {
            datas.add(
                DistanceBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    device_serial = temp.DEVICE_SERIAL,
                    start_date = temp.START_DATE,
                    end_date = temp.END_DATE,
                    distance_meter = temp.DISTANCE_METER!!,
                    source = temp.SOURCE,
                    source_text = temp.SOURCE_TEXT
                )
            )
        }
        return RequestBioLog("bio_distance", datas)
    }

    fun selectLogStep(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioStepDao().selectForLog()) {
            datas.add(
                StepBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    device_serial = temp.DEVICE_SERIAL,
                    start_date = temp.START_DATE,
                    end_date = temp.END_DATE,
                    count = temp.COUNT!!,
                    source = temp.SOURCE,
                    source_text = temp.SOURCE_TEXT
                )
            )
        }
        return RequestBioLog("bio_step", datas)
    }

    fun selectLogCho(): RequestBioLog {
        val datas: MutableList<Any> = ArrayList()
        for (temp in db.bioChoDao().selectForLog()) {
            datas.add(
                ChoBioData(
                    get_time = temp.GET_TIME,
                    userId = temp.USER_ID!!,
                    device_id = temp.DEVICE_ID!!,
                    device_serial = temp.DEVICE_SERIAL,
                    cho_type = temp.CHO_TYPE,
                    cho_value = temp.CHO_VALUE,
                )
            )
        }
        return RequestBioLog("bio_cho", datas)
    }
}
