package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TCMA_DEVICE_CONFIG")
data class DeviceConfig(
    @PrimaryKey @ColumnInfo(name = "DEVICE_ID") var DEVICE_ID: String,
    @ColumnInfo(name = "DEVICE_ORDER") var DEVICE_ORDER: String?,
    @ColumnInfo(name = "DEVICE_TYPE") var DEVICE_TYPE: String,
    @ColumnInfo(name = "DEVICE_TYPE_NAME") var DEVICE_TYPE_NAME: String?,
    @ColumnInfo(name = "DEVICE_NAME") var DEVICE_NAME: String?,
    @ColumnInfo(name = "DEVICE_MODEL") var DEVICE_MODEL: String?,
    @ColumnInfo(name = "DEVICE_VERSION") var DEVICE_VERSION: String?,
    @ColumnInfo(name = "DEVICE_COMPANY") var DEVICE_COMPANY: String?,
    @ColumnInfo(name = "APP_PACKAGE") var APP_PACKAGE: String?,
    @ColumnInfo(name = "APP_ACTIVITY") var APP_ACTIVITY: String?,
    @ColumnInfo(name = "APP_ICON",) var APP_ICON: String?,
)

