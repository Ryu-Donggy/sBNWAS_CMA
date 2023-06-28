package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "TCMA_BIO_SPEED", primaryKeys = ["GET_TIME", "DEVICE_ID", "USER_ID"])
data class BioSpeed(
    @ColumnInfo(name = "GET_TIME") var GET_TIME: String,
    @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String,
    @ColumnInfo(name = "USER_ID") var USER_ID: String,
    @ColumnInfo(name = "START_DATE") var START_DATE: String?,
    @ColumnInfo(name = "END_DATE") var END_DATE: String?,
    @ColumnInfo(name = "SPEED_M_SECOND") var SPEED_M_SECOND: String,
    @ColumnInfo(name = "SOURCE") var SOURCE: String?,
    @ColumnInfo(name = "SOURCE_TEXT") var SOURCE_TEXT: String?,
    @ColumnInfo(name = "DEVICE_SERIAL") var DEVICE_SERIAL: String,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity

