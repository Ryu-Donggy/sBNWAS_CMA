package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioGyr
import com.familidata.sbnwas_cma.room.entity.BioOxs


@Dao
interface BioOxsDao {

    @get:Query("SELECT * FROM TCMA_BIO_OXS")
    val all: List<BioGyr>

    @Query("SELECT COUNT(*) from TCMA_BIO_OXS WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_OXS WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioOxs)


    @Query("SELECT * FROM TCMA_BIO_OXS WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate AND USER_ID = :userId ORDER BY GET_TIME DESC")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioOxs>

    @Query("SELECT COUNT(*) FROM TCMA_BIO_OXS WHERE datetime(GET_TIME) BETWEEN datetime(:fromDateString) AND datetime(:toDateString) AND USER_ID = :userId")
    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int

    @Query("DELETE FROM TCMA_BIO_OXS")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_OXS WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioOxs>

    @Query("UPDATE TCMA_BIO_OXS SET SAVE_TO_SERVER_YN= 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId")
    fun serverSent(getTime: String, userId: String)

    @Query("SELECT * FROM TCMA_BIO_OXS WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioOxs

    @Query("SELECT * FROM TCMA_BIO_OXS WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String): List<BioOxs>

    @Query("SELECT * FROM TCMA_BIO_OXS WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String): BioOxs

    @Query("DELETE FROM TCMA_BIO_OXS WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)

    @Query("SELECT * FROM TCMA_BIO_OXS WHERE USER_ID = :userId "
            + " AND DATE(GET_TIME) >= (SELECT DATE(MAX(GET_TIME), '-6 DAY') FROM TCMA_BIO_OXS WHERE USER_ID = :userId) "
            + " ORDER BY GET_TIME ASC")
    fun selectForMonitorForChart(userId: String): List<BioOxs>

    @Query("UPDATE TCMA_BIO_OXS SET TAG1 = :tag1 WHERE GET_TIME = :getTime and USER_ID = :userId")
    fun updateTag(userId: String, getTime: String, tag1: String)

}
