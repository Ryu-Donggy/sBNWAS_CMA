package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.*

@Dao
interface BioSleepSessionDao {

    @get:Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION")
    val all: List<BioSleepSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: BioSleepSession)

    @Query("SELECT COUNT(*) FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId AND STRFTIME('%Y-%m-%d', datetime(GET_TIME/1000, 'UNIXEPOCH', 'LOCALTIME')) = :date")
    fun countAtDay(userId: String, date: String): Int

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId AND STRFTIME('%Y-%m-%d', datetime(GET_TIME/1000, 'UNIXEPOCH', 'LOCALTIME')) = :date")
    fun selectAtDay(userId: String, date: String): List<BioSleepSession>

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectLastOne(userId: String): BioSleepSession

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String): List<BioSleepSession>

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String): BioSleepSession

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId AND SESSION_ID = :sessionID ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String, sessionID: Long): BioSleepSession

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioSleepSession

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE USER_ID = :userId AND GET_TIME >= :weekAgo ORDER BY GET_TIME ASC")
    fun selectForMonitorForChart(userId: String, weekAgo: Long): List<BioSleepSession>

    @Query("SELECT * FROM TCMA_BIO_SLEEP_SESSION WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioSleepSession>

    @Query("UPDATE TCMA_BIO_SLEEP_SESSION SET SAVE_TO_SERVER_YN= 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId")
    fun serverSent(getTime: String, userId: String)
}