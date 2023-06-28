package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_BIO_BAS", primaryKeys = ["GET_TIME", "DEVICE_ID", "USER_ID"])
data class BioBas(
    @ColumnInfo(name = "GET_TIME") var GET_TIME: String,
    @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String,
    @ColumnInfo(name = "USER_ID") var USER_ID: String,
    @ColumnInfo(name = "DATA_TYPE") var DATA_TYPE: String?,
    @ColumnInfo(name = "BPM") var BPM: String?,
    @ColumnInfo(name = "REQULARITY") var REQULARITY: String?,
    @ColumnInfo(name = "FTPPATH") var FTPPATH: String?,
    @ColumnInfo(name = "POSITION") var POSITION: String?,
    @ColumnInfo(name = "DIAGNOSIS") var DIAGNOSIS: String?,
    @ColumnInfo(name = "STATUS") var STATUS: String?,
    @ColumnInfo(name = "DEVICE_SERIAL") var DEVICE_SERIAL: String?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity
