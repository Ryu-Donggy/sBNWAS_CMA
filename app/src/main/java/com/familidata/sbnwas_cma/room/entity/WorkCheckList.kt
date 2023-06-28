package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_WORK_CHECK_LIST")
data class WorkCheckList(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") var ID: Long = 0,
    @ColumnInfo(name = "WORK_ID") var WORK_ID : String,
    @ColumnInfo(name = "CHECK_TYPE") var CHECK_TYPE : String,
    @ColumnInfo(name = "CHECK_ORDER") var CHECK_ORDER : String?,
    @ColumnInfo(name = "START_TIME") var START_TIME : String?,
    @ColumnInfo(name = "END_TIME") var END_TIME : String?,
    @ColumnInfo(name = "BAS_CHECK") var BAS_CHECK : String?,
    @ColumnInfo(name = "BLP_CHECK") var BLP_CHECK : String?,
    @ColumnInfo(name = "ECG_CHECK") var ECG_CHECK : String?,
    @ColumnInfo(name = "HTR_CHECK") var HTR_CHECK : String?,
    @ColumnInfo(name = "OXS_CHECK") var OXS_CHECK : String?,
    @ColumnInfo(name = "UPDATE_DTTM") var UPDATE_DTTM: String?,
)
