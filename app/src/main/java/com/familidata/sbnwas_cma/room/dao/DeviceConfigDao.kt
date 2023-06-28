package com.familidata.sbnwas_cma.room.dao

import android.bluetooth.BluetoothClass.Device
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.DeviceConfig

@Dao
interface DeviceConfigDao {

    @get:Query("SELECT * FROM TCMA_DEVICE_CONFIG")
    val all: List<DeviceConfig>

    @get:Query("SELECT * FROM TCMA_DEVICE_CONFIG WHERE DEVICE_TYPE = 'CHO'")
    val allCho: List<DeviceConfig>

    @get:Query("SELECT * FROM TCMA_DEVICE_CONFIG WHERE DEVICE_TYPE != 'WTH' AND DEVICE_TYPE != 'APP'")
    val selectWithoutWatchNApp: List<DeviceConfig>

    @get:Query("SELECT * FROM TCMA_DEVICE_CONFIG WHERE DEVICE_TYPE = 'BDY'")
    val selectBdy: List<DeviceConfig>

    @Query("SELECT COUNT(*) FROM TCMA_DEVICE_CONFIG")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: DeviceConfig)


    @Query("SELECT DEVICE_ID FROM TCMA_DEVICE_CONFIG WHERE DEVICE_TYPE = 'WTH'")
    fun selectWatch(): String

    @Query("SELECT CASE WHEN A.CNT = 0 THEN 'http://www.sbnwas.com/images/cma/icon/unknown.png' ELSE A.APP_ICON END AS APP_ICON FROM (SELECT COUNT(*) AS CNT, APP_ICON FROM TCMA_DEVICE_CONFIG WHERE DEVICE_ID = :deviceId) A")
    fun selectIconAddr(deviceId: String): String


    @Query("DELETE FROM TCMA_DEVICE_CONFIG")
    fun delete()
}