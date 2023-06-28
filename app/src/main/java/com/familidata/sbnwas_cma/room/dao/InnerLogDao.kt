package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.InnerLog


@Dao
interface InnerLogDao {

    @get:Query("SELECT * FROM INNER_LOG")
    val all: List<InnerLog>

    @Query("SELECT COUNT(*) from INNER_LOG")
    fun count(): Int


    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(row: InnerLog)

    @Query("INSERT INTO INNER_LOG (LOG) VALUES(:LOG)")
    fun insert(LOG: String): Long

    @Query("DELETE FROM INNER_LOG")
    fun delete()
}
