package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.*


@Dao
interface BioEcgDao {

    @get:Query("SELECT * FROM TCMA_BIO_ECG")
    val all: List<BioEcg>

    @Query("SELECT COUNT(*) from TCMA_BIO_ECG WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_ECG WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioEcg)

    @Query("SELECT * FROM TCMA_BIO_ECG WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate AND USER_ID = :userId ORDER BY GET_TIME DESC")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioEcg>

    @Query("SELECT COUNT(*) FROM TCMA_BIO_ECG WHERE datetime(GET_TIME) BETWEEN datetime(:fromDateString) AND datetime(:toDateString) AND USER_ID = :userId")
    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int

    @Query("DELETE FROM TCMA_BIO_ECG")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_ECG WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioEcg>

    @Query("UPDATE TCMA_BIO_ECG SET SAVE_TO_SERVER_YN = 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId")
    fun serverSent(getTime: String, userId: String)

    @Query("SELECT * FROM TCMA_BIO_ECG WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId AND datetime(GET_TIME) > datetime(:today)   ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String, today: String): BioEcg
}
