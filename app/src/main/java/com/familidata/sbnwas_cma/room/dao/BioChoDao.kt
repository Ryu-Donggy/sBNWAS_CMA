package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.*


@Dao
interface BioChoDao {

    @get:Query("SELECT * FROM TCMA_BIO_CHO")
    val all: List<BioCho>

    @Query("SELECT COUNT(*) from TCMA_BIO_CHO WHERE USER_ID = :userId AND CHO_TYPE = :choType ")
    fun count(userId: String, choType: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_CHO WHERE date(GET_TIME) = :date AND USER_ID = :userId AND CHO_TYPE = :choType")
    fun countADay(date: String, userId: String, choType: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioCho)

    @Query("SELECT * FROM TCMA_BIO_CHO WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate AND USER_ID = :userId AND CHO_TYPE = :choType ORDER BY GET_TIME DESC")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String, choType: String): List<BioCho>

    @Query("SELECT * FROM TCMA_BIO_CHO WHERE USER_ID = :userId AND CHO_TYPE = :choType ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String, choType: String): List<BioCho>

    @Query("SELECT * FROM TCMA_BIO_CHO WHERE USER_ID = :userId AND CHO_TYPE = :choType ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String, choType: String): BioCho

    @Query("SELECT GET_TIME from TCMA_BIO_CHO WHERE USER_ID = :userId AND DEVICE_ID = :deviceId AND CHO_TYPE = :choType ORDER BY GET_TIME DESC LIMIT 1")
    fun selectLastOneGetTime(userId: String, deviceId: String, choType: String): String

    @Query("SELECT COUNT(*) FROM TCMA_BIO_CHO WHERE datetime(GET_TIME) BETWEEN datetime(:fromDateString) AND datetime(:toDateString) AND USER_ID = :userId AND CHO_TYPE = :choType")
    fun countByPeriod(fromDateString: String, toDateString: String, userId: String, choType: String): Int

    @Query("DELETE FROM TCMA_BIO_CHO")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_CHO WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioCho>

    @Query("UPDATE TCMA_BIO_CHO SET SAVE_TO_SERVER_YN = 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId AND DEVICE_ID = :deviceId AND CHO_TYPE = :choType")
    fun serverSent(getTime: String, userId: String, deviceId:String, choType: String)

    @Query("SELECT * FROM TCMA_BIO_CHO WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId  AND CHO_TYPE = :choType ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String, choType: String): BioCho

    @Query("DELETE FROM TCMA_BIO_CHO WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId AND CHO_TYPE = :choType")
    fun deleteOne(get_time: String, device_id: String, userId: String, choType: String)


    @Query(
        "SELECT * FROM TCMA_BIO_CHO " +
                " WHERE USER_ID = :userId "
                + " AND DATE(GET_TIME) >= (SELECT DATE(MAX(GET_TIME), '-6 DAY') FROM TCMA_BIO_CHO WHERE USER_ID = :userId AND CHO_TYPE = :choType) "
                + " AND CHO_TYPE = :choType"
                + " ORDER BY GET_TIME ASC"
    )
    fun selectForMonitorForChart(userId: String, choType: String): List<BioCho>
}
