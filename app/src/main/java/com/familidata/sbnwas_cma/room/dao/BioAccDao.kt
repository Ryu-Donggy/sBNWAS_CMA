package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioAcc
import com.familidata.sbnwas_cma.room.entity.BioBlp


@Dao
interface BioAccDao {

    @get:Query("SELECT * FROM TCMA_BIO_ACC")
    val all: List<BioAcc>

    @Query("SELECT COUNT(*) from TCMA_BIO_ACC WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_ACC WHERE date(GET_TIME) = :date and USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: BioAcc)

    @Query("SELECT * FROM TCMA_BIO_ACC WHERE date(GET_TIME) >= :fromWorkDate AND date(GET_TIME) <= :endWorkDate and USER_ID = :userId ORDER BY GET_TIME DESC LIMIT 1000")
    fun selectByDate(fromWorkDate: String, endWorkDate: String, userId: String): List<BioAcc>

    @Query("DELETE FROM TCMA_BIO_ACC")
    fun delete()

    @Query("SELECT * FROM TCMA_BIO_ACC WHERE  DEVICE_ID = :deviceId AND USER_ID = :userId ORDER by GET_TIME DESC LIMIT 1")
    fun selectLast(deviceId: String, userId: String): BioAcc
}
