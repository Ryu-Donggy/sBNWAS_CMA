package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioGps
import com.familidata.sbnwas_cma.room.entity.BioGyr


@Dao
interface BioGyrDao {

    @get:Query("SELECT * FROM TCMA_BIO_GYR")
    val all: List<BioGyr>

    @Query("SELECT COUNT(*) from TCMA_BIO_GYR WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_GYR WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioGyr)

    @Query("SELECT * FROM TCMA_BIO_GYR WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate AND USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioGyr>

    @Query("DELETE FROM TCMA_BIO_GYR")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_GYR WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioGyr
}
