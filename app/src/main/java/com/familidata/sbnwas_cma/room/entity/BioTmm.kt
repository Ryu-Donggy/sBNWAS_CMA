package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "TCMA_BIO_TMM", primaryKeys = ["GET_TIME", "DEVICE_ID", "USER_ID"])
data class BioTmm(
    @ColumnInfo(name = "GET_TIME") var GET_TIME: String,
    @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String,
    @ColumnInfo(name = "USER_ID") var USER_ID: String,
    @ColumnInfo(name = "TEMPERATURE") var TEMPERATURE: String?,
    @ColumnInfo(name = "DEVICE_SERIAL") var DEVICE_SERIAL: String?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity