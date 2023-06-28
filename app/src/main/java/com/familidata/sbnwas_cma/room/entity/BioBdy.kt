package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_BIO_BDY", primaryKeys = ["GET_TIME", "DEVICE_ID", "USER_ID"])
data class BioBdy(
    @ColumnInfo(name = "GET_TIME") var GET_TIME: String,
    @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String,
    @ColumnInfo(name = "USER_ID") var USER_ID: String,
    @ColumnInfo(name = "HT") var HT: String?,
    @ColumnInfo(name = "WT") var WT: String?,
    @ColumnInfo(name = "BFM") var BFM: String?,
    @ColumnInfo(name = "SMM") var SMM: String?,
    @ColumnInfo(name = "PBF") var PBF: String?,
    @ColumnInfo(name = "BMI") var BMI: String?,
    @ColumnInfo(name = "WHR") var WHR: String?,
    @ColumnInfo(name = "GENDER") var GENDER: String?,
    @ColumnInfo(name = "VFL") var VFL: String?,
    @ColumnInfo(name = "WT_MIN") var WT_MIN: String?,
    @ColumnInfo(name = "WT_MAX") var WT_MAX: String?,
    @ColumnInfo(name = "SMM_MIN") var SMM_MIN: String?,
    @ColumnInfo(name = "SMM_MAX") var SMM_MAX: String?,
    @ColumnInfo(name = "BFM_MIN") var BFM_MIN: String?,
    @ColumnInfo(name = "BFM_MAX") var BFM_MAX: String?,
    @ColumnInfo(name = "VFL_MIN") var VFL_MIN: String?,
    @ColumnInfo(name = "VFL_MAX") var VFL_MAX: String?,
    @ColumnInfo(name = "IBMI") var IBMI: String?,
    @ColumnInfo(name = "BMI_MIN") var BMI_MIN: String?,
    @ColumnInfo(name = "BMI_MAX") var BMI_MAX: String?,
    @ColumnInfo(name = "IPBF") var IPBF: String?,
    @ColumnInfo(name = "PBF_MIN") var PBF_MIN: String?,
    @ColumnInfo(name = "PBF_MAX") var PBF_MAX: String?,
    @ColumnInfo(name = "IWHR") var IWHR: String?,
    @ColumnInfo(name = "WHR_MIN") var WHR_MIN: String?,
    @ColumnInfo(name = "WHR_MAX") var WHR_MAX: String?,
    @ColumnInfo(name = "BMR") var BMR: String?,
    @ColumnInfo(name = "BMR_MIN") var BMR_MIN: String?,
    @ColumnInfo(name = "BMR_MAX") var BMR_MAX: String?,
    @ColumnInfo(name = "WC") var WC: String?,
    @ColumnInfo(name = "MC") var MC: String?,
    @ColumnInfo(name = "FC") var FC: String?,
    @ColumnInfo(name = "FS") var FS: String?,
    @ColumnInfo(name = "IBFM") var IBFM: String?,
    @ColumnInfo(name = "IFFM") var IFFM: String?,
    @ColumnInfo(name = "MAX_WT") var MAX_WT: String?,
    @ColumnInfo(name = "MIN_WT") var MIN_WT: String?,
    @ColumnInfo(name = "MAX_SMM") var MAX_SMM: String?,
    @ColumnInfo(name = "MIN_SMM") var MIN_SMM: String?,
    @ColumnInfo(name = "MAX_BFM") var MAX_BFM: String?,
    @ColumnInfo(name = "MIN_BFM") var MIN_BFM: String?,
    @ColumnInfo(name = "MAX_BMI") var MAX_BMI: String?,
    @ColumnInfo(name = "MIN_BMI") var MIN_BMI: String?,
    @ColumnInfo(name = "MAX_PBF") var MAX_PBF: String?,
    @ColumnInfo(name = "MIN_PBF") var MIN_PBF: String?,
    @ColumnInfo(name = "TBW") var TBW: String?,
    @ColumnInfo(name = "DESCRIPTION") var DESCRIPTION: String?,
    @ColumnInfo(name = "DEVICE_SERIAL") var DEVICE_SERIAL: String?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity