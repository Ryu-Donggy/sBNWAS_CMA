package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_WORK_PLAN")
data class WorkPlan(
    @PrimaryKey @ColumnInfo(name = "workId") var workId: String,
    @ColumnInfo(name = "workDate") var workDate: String,
    @ColumnInfo(name = "workPlace") var workPlace: String,
    @ColumnInfo(name = "workRole") var workRole: String,
    @ColumnInfo(name = "startTime") var startTime: String,
    @ColumnInfo(name = "endTime") var endTime: String,
    @ColumnInfo(name = "workStartTime") var workStartTime: String = "",
    @ColumnInfo(name = "workEndTime") var workEndTime: String = "",
    @ColumnInfo(name = "workStatus") var workStatus: String,
    @ColumnInfo(name = "workStatusText") var workStatusText: String,
)