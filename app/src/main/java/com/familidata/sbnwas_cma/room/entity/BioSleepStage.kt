package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "TCMA_BIO_SLEEP_STAGE", primaryKeys = ["STAGE_ID", "SESSION_ID"])
data class BioSleepStage(
    @ColumnInfo(name = "STAGE_ID") var STAGE_ID: Long,
    @ColumnInfo(name = "SESSION_ID") var SESSION_ID: Long,
    @ColumnInfo(name = "USER_ID") var USER_ID: String?,
    @ColumnInfo(name = "STAGE_CODE") var STAGE_CODE: String?,
    @ColumnInfo(name = "STAGE_TEXT") var STAGE_TEXT: String?,
    @ColumnInfo(name = "STAGE_START_DATE") var STAGE_START_DATE: String?,
    @ColumnInfo(name = "STAGE_END_DATE") var STAGE_END_DATE: String?,
    @ColumnInfo(name = "STAGE_PERIOD") var STAGE_PERIOD: Long?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
    @ColumnInfo(name = "SAVE_TO_SERVER_YN", defaultValue = "N") var SAVE_TO_SERVER_YN: String?,
) : ISuperLogEntity