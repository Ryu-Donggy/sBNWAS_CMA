package com.familidata.sbnwas_cma.room.dao

import android.bluetooth.BluetoothClass.Device
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.CmaNotice
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan

@Dao
interface CmaNoticeDao {

    @get:Query("SELECT * FROM TCMA_NOTICE")
    val all: List<CmaNotice>

    @Query("SELECT COUNT(*) FROM TCMA_NOTICE")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: CmaNotice)

    @Query("DELETE FROM TCMA_NOTICE")
    fun delete()

    @Query("SELECT * FROM TCMA_NOTICE WHERE NOTICE_SOURCE = :type AND CREATE_DTTM <= :workDate AND CREATE_DTTM >= :monthAgoDate ORDER BY CREATE_DTTM DESC")
    fun selectForList(type: String, workDate: String, monthAgoDate: String): List<CmaNotice>
}