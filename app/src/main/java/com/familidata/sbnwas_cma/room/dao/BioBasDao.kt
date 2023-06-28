package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.*


@Dao
interface BioBasDao {

    @get:Query("SELECT * FROM TCMA_BIO_BAS")
    val all: List<BioBas>

    @Query("SELECT COUNT(*) from TCMA_BIO_BAS WHERE USER_ID = :userId AND DATA_TYPE = :dataType")
    fun count(userId: String, dataType: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_BAS WHERE date(GET_TIME) = :date AND USER_ID = :userId AND DATA_TYPE = :dataType")
    fun countADay(date: String, userId: String, dataType: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioBas)

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE date(CREATE_DTTM) >= :fromWorkDate AND date(CREATE_DTTM) <= :endWorkDate AND USER_ID = :userId AND DATA_TYPE = :dataType ORDER BY CREATE_DTTM DESC")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String, dataType: String): List<BioBas>

    @Query("SELECT COUNT(*) FROM TCMA_BIO_BAS WHERE datetime(GET_TIME) BETWEEN datetime(:fromDateString) AND datetime(:toDateString) AND USER_ID = :userId AND DATA_TYPE = :dataType")
    fun countByPeriod(fromDateString: String, toDateString: String, userId: String, dataType: String): Int

    @Query("DELETE FROM TCMA_BIO_BAS")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioBas>

    @Query("UPDATE TCMA_BIO_BAS SET SAVE_TO_SERVER_YN= 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId AND DEVICE_ID = :deviceId")
    fun serverSent(getTime: String, userId: String, deviceId:String)

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId AND DATA_TYPE = :dataType ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String, dataType: String): BioBas

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE DATA_TYPE = 'H' AND USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForHmsMonitor(userId: String): List<BioBas>

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE DATA_TYPE != 'H' AND USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForLmsMonitor(userId: String): List<BioBas>

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE DATA_TYPE = 'H' AND USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneHmsForMonitor(userId: String): BioBas

    @Query("SELECT * FROM TCMA_BIO_BAS WHERE DATA_TYPE != 'H' AND USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneLmsForMonitor(userId: String): BioBas

    @Query("DELETE FROM TCMA_BIO_BAS WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)

}
