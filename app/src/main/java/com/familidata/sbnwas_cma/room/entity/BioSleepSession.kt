package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "TCMA_BIO_SLEEP_SESSION", primaryKeys = ["SESSION_ID"])
data class BioSleepSession(
    @ColumnInfo(name = "SESSION_ID") var SESSION_ID: Long,
    @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String?,
    @ColumnInfo(name = "USER_ID") var USER_ID: String?,
    @ColumnInfo(name = "GET_TIME") var GET_TIME: String?,
    @ColumnInfo(name = "SESSION_START_DATE") var SESSION_START_DATE: String?,
    @ColumnInfo(name = "SESSION_END_DATE") var SESSION_END_DATE: String?,
    @ColumnInfo(name = "SESSION_PERIOD") var SESSION_PERIOD: Long?,
    @ColumnInfo(name = "SESSION_DEEP") var SESSION_DEEP: Long?,
    @ColumnInfo(name = "SESSION_LIGHT") var SESSION_LIGHT: Long?,
    @ColumnInfo(name = "SESSION_REM") var SESSION_REM: Long?,
    @ColumnInfo(name = "SESSION_ETC") var SESSION_ETC: Long?,
    @ColumnInfo(name = "SESSION_SOURCE") var SESSION_SOURCE: String?,
    @ColumnInfo(name = "SESSION_SOURCE_TEXT") var SESSION_SOURCE_TEXT: String?,
    @ColumnInfo(name = "DEVICE_SERIAL") var DEVICE_SERIAL: String?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity
