package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.Ap
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.BioGyr


@Dao
interface ApDao {

    @get:Query("SELECT * FROM TCMA_AP")
    val all: List<Ap>

    @Query("SELECT COUNT(*) from TCMA_WORK_PLAN P INNER JOIN TCMA_AP A ON P.WORKID = A.WORKID AND UPPER(REPLACE(REPLACE(:bssid, '-', ''), ':', '')) = UPPER(REPLACE(REPLACE(A.MACADDRESS, '-', ''), ':', '')) WHERE P.WORKSTATUS = 1")
    fun count(bssid: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: Ap)

    @Query("DELETE FROM TCMA_AP WHERE workId = :workId")
    fun deleteOne(workId: String)

    @Query("DELETE FROM TCMA_AP")
    fun delete()
}
