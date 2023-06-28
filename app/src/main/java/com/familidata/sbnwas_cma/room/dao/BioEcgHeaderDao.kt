package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioEcgHeader
import com.familidata.sbnwas_cma.room.entity.BioHtr

@Dao
interface BioEcgHeaderDao {

    @get:Query("SELECT * FROM TCMA_BIO_ECG_HEADER")
    val all: List<BioEcgHeader>

    @Query("DELETE FROM TCMA_BIO_ECG_HEADER")
    fun delete()

    @Query("SELECT COUNT(*) from TCMA_BIO_ECG_HEADER WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioEcgHeader)

    @Query("SELECT * FROM TCMA_BIO_ECG_HEADER WHERE DEVICE_ID = :deviceId AND USER_ID = :userId AND datetime(GET_TIME) > datetime(:today) ORDER BY GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String, today: String): BioEcgHeader

    @Query("SELECT COUNT(*) FROM TCMA_BIO_ECG_HEADER WHERE datetime(GET_TIME) BETWEEN datetime(:fromDateString) AND datetime(:toDateString) AND USER_ID = :userId")
    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int

    @Query("SELECT * FROM TCMA_BIO_ECG_HEADER WHERE DEVICE_ID = :deviceId AND USER_ID = :userId ORDER BY GET_TIME DESC")
    fun selectAll(deviceId: String, userId: String): List<BioEcgHeader>

    @Query("SELECT * FROM TCMA_BIO_ECG_HEADER WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectForMonitor(userId: String): List<BioEcgHeader>

    @Query("SELECT * FROM TCMA_BIO_ECG_HEADER WHERE USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1")
    fun selectOneForMonitor(userId: String): BioEcgHeader

    @Query("DELETE FROM TCMA_BIO_ECG_HEADER WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)


}