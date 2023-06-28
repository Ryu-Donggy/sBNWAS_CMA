package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.*

@Dao
interface BioBdyDao {

    @get:Query("SELECT * FROM TCMA_BIO_BDY")
    val all: List<BioBdy>

    @Query("SELECT COUNT(*) from TCMA_BIO_BDY WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_BDY WHERE date(GET_TIME) = :date and USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioBdy)

    @Query("SELECT * FROM TCMA_BIO_BDY WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate and USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioBdy>

    @Query("DELETE FROM TCMA_BIO_BDY")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_BDY WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioBdy>

    @Query("UPDATE TCMA_BIO_BDY SET SAVE_TO_SERVER_YN = 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId")
    fun serverSent(getTime: String, userId: String)

    @Query("SELECT * FROM TCMA_BIO_BDY WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioBdy

    @Query("SELECT * FROM TCMA_BIO_BDY WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String): List<BioBdy>

    @Query("SELECT * FROM TCMA_BIO_BDY WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String): BioBdy

    @Query("DELETE FROM TCMA_BIO_BDY WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)

    @Query(" SELECT * FROM TCMA_BIO_BDY WHERE USER_ID = :userId "
            + " AND DATE(GET_TIME) >= (SELECT DATE(MAX(GET_TIME), '-6 DAY') FROM TCMA_BIO_BDY WHERE USER_ID = :userId) "
            + " ORDER BY GET_TIME ASC")
    fun selectForMonitorForChart(userId: String): List<BioBdy>
}