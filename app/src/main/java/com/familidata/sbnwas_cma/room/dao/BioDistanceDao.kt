package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioDistance

@Dao
interface BioDistanceDao {

    @get:Query("SELECT * FROM TCMA_BIO_DISTANCE")
    val all: List<BioDistance>

    @Query("SELECT COUNT(*) from TCMA_BIO_DISTANCE WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_DISTANCE WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: BioDistance)

    @Query("DELETE FROM TCMA_BIO_DISTANCE WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)

    @Query("DELETE FROM TCMA_BIO_DISTANCE")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_DISTANCE WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioDistance

    @Query("SELECT * FROM TCMA_BIO_DISTANCE WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String): List<BioDistance>

    @Query("SELECT * FROM TCMA_BIO_DISTANCE WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String): BioDistance

    @Query("SELECT * FROM TCMA_BIO_DISTANCE WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioDistance>

    @Query("UPDATE TCMA_BIO_DISTANCE SET SAVE_TO_SERVER_YN= 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId AND DEVICE_ID = :deviceId")
    fun serverSent(getTime: String, userId: String, deviceId:String)

    @Query("SELECT * FROM TCMA_BIO_DISTANCE WHERE USER_ID = :userId "
            + " AND DATE(GET_TIME) >= (SELECT DATE(MAX(GET_TIME), '-6 DAY') FROM TCMA_BIO_DISTANCE WHERE USER_ID = :userId) "
            + " ORDER BY GET_TIME ASC")
    fun selectForMonitorForChart(userId: String): List<BioDistance>
}