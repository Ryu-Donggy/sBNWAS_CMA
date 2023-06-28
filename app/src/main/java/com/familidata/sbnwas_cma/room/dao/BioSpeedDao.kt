package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioSpeed

@Dao
interface BioSpeedDao {

    @get:Query("SELECT * FROM TCMA_BIO_SPEED")
    val all: List<BioSpeed>

    @Query("SELECT COUNT(*) from TCMA_BIO_SPEED WHERE USER_ID = :userId")
    fun count(userId: String): Int

    @Query("SELECT COUNT(*) from TCMA_BIO_SPEED WHERE date(GET_TIME) = :date AND USER_ID = :userId")
    fun countADay(date: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: BioSpeed)

    @Query("DELETE FROM TCMA_BIO_SPEED WHERE GET_TIME = :get_time AND DEVICE_ID = :device_id AND USER_ID = :userId")
    fun deleteOne(get_time: String, device_id: String, userId: String)

    @Query("DELETE FROM TCMA_BIO_SPEED")
    fun delete()

}