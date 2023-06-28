package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.BioEcg
import com.familidata.sbnwas_cma.room.entity.BioGps
import com.familidata.sbnwas_cma.room.entity.BioGyr


@Dao
interface BioGpsDao {

    @get:Query("SELECT * FROM TCMA_BIO_GPS")
    val all: List<BioGyr>

    @Query("SELECT COUNT(*) from TCMA_BIO_GPS WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_GPS WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioGps)


    @Query("SELECT * FROM TCMA_BIO_GPS WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate AND USER_ID = :userId ORDER BY GET_TIME DESC")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioGps>

    @Query("DELETE FROM TCMA_BIO_GPS")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_GPS WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioGps>

    @Query("UPDATE TCMA_BIO_GPS SET SAVE_TO_SERVER_YN = 'Y' WHERE GET_TIME = :getTime AND USER_ID = :userId")
    fun serverSent(getTime: String, userId: String)

    @Query("SELECT * FROM TCMA_BIO_GPS WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioGps
}
