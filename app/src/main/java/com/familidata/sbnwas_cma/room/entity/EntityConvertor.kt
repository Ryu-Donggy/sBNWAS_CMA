package com.familidata.sbnwas_cma.room.entity

import com.familidata.sbnwas_cma.api.req.*


object EntityConvertor {

    fun ecgToReqData(list: List<BioEcg>): RequestBioLog {
        var topic = "bio_ecg"
        val datas: MutableList<Any> = ArrayList()
        for (entity in list) {
            datas.add(
                EcgBioData(
                    get_time = entity.GET_TIME,
                    userId = entity.USER_ID!!,
                    device_id = entity.DEVICE_ID!!,
                    create_dttm = entity.CREATE_DTTM,
                    ecg_index = entity.ECG_INDEX,
                    ecg_data = entity.ECG_DATA,
                    device_serial = entity.DEVICE_SERIAL,
                )
            )

        }
        return RequestBioLog(topic = topic, datas = datas)
    }

    fun htrToReqData(list: List<BioHtr>): RequestBioLog {
        var topic = "bio_htr"
        val datas: MutableList<Any> = ArrayList()
        for (entity in list) {
            datas.add(
                HtrBioData(
                    get_time = entity.GET_TIME,
                    userId = entity.USER_ID!!,
                    device_id = entity.DEVICE_ID!!,
                    heart_rate = entity.HEART_RATE,
                    device_serial = entity.DEVICE_SERIAL,
                )
            )

        }
        return RequestBioLog(topic = topic, datas = datas)
    }

    fun htrAutoToReqData(list: List<BioHtr>): RequestBioLog {
        var topic = "bio_htr_auto"
        val datas: MutableList<Any> = ArrayList()
        for (entity in list) {
            datas.add(
                HtrBioData(
                    get_time = entity.GET_TIME,
                    userId = entity.USER_ID!!,
                    device_id = entity.DEVICE_ID!!,
                    heart_rate = entity.HEART_RATE,
                    device_serial = entity.DEVICE_SERIAL,
                )
            )

        }
        return RequestBioLog(topic = topic, datas = datas)
    }

    fun entitiyToReqData(entity: ISuperLogEntity): RequestBioLog {

        var topic = ""
        val datas: MutableList<Any> = ArrayList()

        var result: Any? = null
        when (entity) {
            is BioAcc -> {
                topic = "bio_acc"
                result = AccBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, axisx = entity.AXISX, axisy = entity.AXISY, axisz = entity.AXISZ, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioBlp -> {
                topic = "bio_blp"
                result = BlpBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, systolic = entity.SYSTOLIC, diastolic = entity.DIASTOLIC, hr = entity.HR, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioBls -> {
                topic = "bio_bls"
                result = BlsBioData(
                    get_time = entity.GET_TIME,
                    userId = entity.USER_ID!!,
                    device_id = entity.DEVICE_ID!!,
                    bls_value = entity.BLS_VALUE,
                    tag1 = entity.TAG1,
                    tag2 = entity.TAG2,
                    tag3 = entity.TAG3,
                    device_serial = entity.DEVICE_SERIAL,
                )

            }

            is BioEcg -> {
                topic = "bio_ecg"
                result = EcgBioData(
                    get_time = entity.GET_TIME,
                    userId = entity.USER_ID!!,
                    device_id = entity.DEVICE_ID!!,
                    create_dttm = entity.CREATE_DTTM,
                    ecg_index = entity.ECG_INDEX,
                    ecg_data = entity.ECG_DATA,
                    device_serial = entity.DEVICE_SERIAL,
                )
            }

            is BioGps -> {
                topic = "bio_gps"
                result = GpsBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, longitude = entity.LONGITUDE, latitude = entity.LATITUDE, altitude = entity.ALTITUDE, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioGyr -> {
                topic = "bio_gyr"
                result = GyrBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, axisx = entity.AXISX, axisy = entity.AXISY, axisz = entity.AXISZ, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioHtr -> {
                topic = "bio_htr"
                result = HtrBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, heart_rate = entity.HEART_RATE, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioOxs -> {
                topic = "bio_oxs"
                result = OxsBioData(get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!, spo2 = entity.SPO2, heart_rate = entity.HEART_RATE, device_serial = entity.DEVICE_SERIAL,)
            }

            is BioBdy -> {
                topic = "bio_bdy"
                result = BdyBioData(
                    get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!,
                    ht = entity.HT,
                    wt = entity.WT,
                    bfm = entity.BFM,
                    smm = entity.SMM,
                    pbf = entity.PBF,
                    bmi = entity.BMI,
                    whr = entity.WHR,
                    gender = entity.GENDER,
                    vfl = entity.VFL,
                    wt_min = entity.WT_MIN,
                    wt_max = entity.WT_MAX,
                    smm_min = entity.SMM_MIN,
                    smm_max = entity.SMM_MAX,
                    bfm_min = entity.BFM_MIN,
                    vfl_min = entity.VFL_MIN,
                    bfm_max = entity.BFM_MAX,
                    vfl_max = entity.VFL_MAX,
                    ibmi = entity.IBMI,
                    bmi_min = entity.BMI_MIN,
                    bmi_max = entity.BMI_MAX,
                    ipbf = entity.IPBF,
                    pbf_min = entity.PBF_MIN,
                    pbf_max = entity.PBF_MAX,
                    iwhr = entity.IWHR,
                    whr_min = entity.WHR_MIN,
                    whr_max = entity.WHR_MAX,
                    bmr = entity.BMR,
                    bmr_min = entity.BMR_MIN,
                    bmr_max = entity.BMR_MAX,
                    wc = entity.WC,
                    mc = entity.MC,
                    fc = entity.FC,
                    fs = entity.FS,
                    ibfm = entity.IBFM,
                    iffm = entity.IFFM,
                    max_wt = entity.MAX_WT,
                    min_wt = entity.MIN_WT,
                    max_smm = entity.MAX_SMM,
                    min_smm = entity.MIN_SMM,
                    max_bfm = entity.MAX_BFM,
                    min_bfm = entity.MIN_BFM,
                    max_bmi = entity.MAX_BMI,
                    min_bmi = entity.MIN_BMI,
                    max_pbf = entity.MAX_PBF,
                    min_pbf = entity.MIN_PBF,
                    tbw = entity.TBW,
                    description = entity.DESCRIPTION,
                    device_serial = entity.DEVICE_SERIAL,
                )
            }

            is BioBas -> {
                topic = "bio_bas"
                result = BasBioData(
                    get_time = entity.GET_TIME, userId = entity.USER_ID!!, device_id = entity.DEVICE_ID!!,
                    data_type = entity.DATA_TYPE,
                    bpm = entity.BPM,
                    reqularity = entity.REQULARITY,
                    ftppath = entity.FTPPATH,
                    position = entity.POSITION,
                    device_serial = entity.DEVICE_SERIAL,
                    diagnosis = entity.DIAGNOSIS,
                    status = entity.STATUS,
                )
            }
        }

        if (result != null) datas.add(result)

        return RequestBioLog(topic = topic, datas = datas)
    }

    fun entitiyToReqDatas(topic: String, list: MutableList<ISuperLogEntity>): RequestBioLog {

        val datas: MutableList<Any> = ArrayList()

        for (entity: ISuperLogEntity in list) {

            var result: Any? = null

            when (entity) {

                is BioHtr -> {
                    result = HtrBioData(
                        get_time = entity.GET_TIME,
                        device_id = entity.DEVICE_ID!!,
                        userId = entity.USER_ID!!,
                        heart_rate = entity.HEART_RATE,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioOxs -> {
                    result = OxsBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        spo2 = entity.SPO2,
                        heart_rate = entity.HEART_RATE,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioBdy -> {
                    result = BdyBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        ht = entity.HT,
                        wt = entity.WT,
                        bfm = entity.BFM,
                        smm = entity.SMM,
                        pbf = entity.PBF,
                        bmi = entity.BMI,
                        whr = entity.WHR,
                        gender = entity.GENDER,
                        vfl = entity.VFL,
                        wt_min = entity.WT_MIN,
                        wt_max = entity.WT_MAX,
                        smm_min = entity.SMM_MIN,
                        smm_max = entity.SMM_MAX,
                        bfm_min = entity.BFM_MIN,
                        vfl_min = entity.VFL_MIN,
                        bfm_max = entity.BFM_MAX,
                        vfl_max = entity.VFL_MAX,
                        ibmi = entity.IBMI,
                        bmi_min = entity.BMI_MIN,
                        bmi_max = entity.BMI_MAX,
                        ipbf = entity.IPBF,
                        pbf_min = entity.PBF_MIN,
                        pbf_max = entity.PBF_MAX,
                        iwhr = entity.IWHR,
                        whr_min = entity.WHR_MIN,
                        whr_max = entity.WHR_MAX,
                        bmr = entity.BMR,
                        bmr_min = entity.BMR_MIN,
                        bmr_max = entity.BMR_MAX,
                        wc = entity.WC,
                        mc = entity.MC,
                        fc = entity.FC,
                        fs = entity.FS,
                        ibfm = entity.IBFM,
                        iffm = entity.IFFM,
                        max_wt = entity.MAX_WT,
                        min_wt = entity.MIN_WT,
                        max_smm = entity.MAX_SMM,
                        min_smm = entity.MIN_SMM,
                        max_bfm = entity.MAX_BFM,
                        min_bfm = entity.MIN_BFM,
                        max_bmi = entity.MAX_BMI,
                        min_bmi = entity.MIN_BMI,
                        max_pbf = entity.MAX_PBF,
                        min_pbf = entity.MIN_PBF,
                        tbw = entity.TBW,
                        description = entity.DESCRIPTION,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioAcc -> {
                    result = AccBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        axisx = entity.AXISX,
                        axisy = entity.AXISY,
                        axisz = entity.AXISZ,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioEcg -> {
                    result = EcgBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        create_dttm = entity.CREATE_DTTM,
                        ecg_index = entity.ECG_INDEX,
                        ecg_data = entity.ECG_DATA,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioGyr -> {
                    result = GyrBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        axisx = entity.AXISX,
                        axisy = entity.AXISY,
                        axisz = entity.AXISZ,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

                is BioGps -> {
                    result = GpsBioData(
                        get_time = entity.GET_TIME,
                        userId = entity.USER_ID!!,
                        device_id = entity.DEVICE_ID!!,
                        longitude = entity.LONGITUDE,
                        latitude = entity.LATITUDE,
                        altitude = entity.ALTITUDE,
                        device_serial = entity.DEVICE_SERIAL,
                    )
                }

            } // when

            if (result != null) datas.add(result)

        } // for

        return RequestBioLog(topic = topic, datas = datas)
    }

}
