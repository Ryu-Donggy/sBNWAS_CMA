package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioSleepSession
import com.familidata.sbnwas_cma.room.entity.BioSleepStage

@Dao
interface BioSleepStageDao {

    @get:Query("SELECT * FROM TCMA_BIO_SLEEP_STAGE")
    val all: List<BioSleepSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: BioSleepStage)

    @Query("SELECT COUNT(*) FROM TCMA_BIO_SLEEP_STAGE WHERE SESSION_ID = :sessionId")
    fun count(sessionId: Long): Int

    @Query("SELECT * FROM TCMA_BIO_SLEEP_STAGE WHERE SESSION_ID = :sessionId ORDER BY STAGE_START_DATE ASC")
    fun selectAtSession(sessionId: Long): List<BioSleepStage>

    @Query("SELECT * FROM TCMA_BIO_SLEEP_STAGE WHERE SAVE_TO_SERVER_YN = 'N' ORDER BY CREATE_DTTM ASC")
    fun selectForLog(): List<BioSleepStage>


    @Query("UPDATE TCMA_BIO_SLEEP_STAGE SET SAVE_TO_SERVER_YN= 'Y' WHERE STAGE_ID = :stageId AND SESSION_ID = :sessionId")
    fun serverSent(stageId: Long, sessionId: Long)
}