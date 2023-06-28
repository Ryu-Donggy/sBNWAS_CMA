package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "INNER_LOG")
data class InnerLog(
    @PrimaryKey @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") var id: Long,
    @ColumnInfo(defaultValue = "(datetime('now'))") var dt2: String,
    @ColumnInfo(name = "LOG") var LOG: String?,
)