package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_AP", primaryKeys = ["workId", "apId"])
data class Ap(
    @ColumnInfo(name = "workId") var workId: String,
    @ColumnInfo(name = "apId") var apId: String,
    @ColumnInfo(name = "accessId") var accessId: String,
    @ColumnInfo(name = "macAddress") var macAddress: String,
    @ColumnInfo(name = "imei") var imei: String,
)
