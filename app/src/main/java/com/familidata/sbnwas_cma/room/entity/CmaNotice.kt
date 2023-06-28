package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_NOTICE")
data class CmaNotice(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") var ID: Long = 0,
    @ColumnInfo(name = "NOTICE_TITLE") var NOTICE_TITLE: String?,
    @ColumnInfo(name = "NOTICE_BODY") var NOTICE_BODY: String?,
    @ColumnInfo(name = "NOTICE_SOURCE") var NOTICE_SOURCE: String?,
    @ColumnInfo(name = "CREATE_DTTM") var CREATE_DTTM: String?,
)
