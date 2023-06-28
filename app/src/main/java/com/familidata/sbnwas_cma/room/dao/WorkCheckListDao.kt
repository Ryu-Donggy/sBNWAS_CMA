package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.BioAcc
import com.familidata.sbnwas_cma.room.entity.WorkCheckList

@Dao
interface WorkCheckListDao {

    @get:Query("SELECT * FROM TCMA_WORK_CHECK_LIST")
    val all: List<WorkCheckList>

    @Query("SELECT COUNT(*) from TCMA_WORK_CHECK_LIST WHERE WORK_ID = :workId")
    fun countByWorkId(workId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(row: WorkCheckList)

    @Query("SELECT * FROM TCMA_WORK_CHECK_LIST WHERE WORK_ID = :workId ORDER BY CHECK_TYPE, CHECK_ORDER")
    fun selectByWorkId(workId: String): List<WorkCheckList>

    @Query("UPDATE TCMA_WORK_CHECK_LIST SET BAS_CHECK = 'Y', UPDATE_DTTM = CURRENT_TIMESTAMP WHERE BAS_CHECK = 'N' AND :checkTime BETWEEN START_TIME AND END_TIME")
    fun updateBasCheck(checkTime: String): Int

    @Query("UPDATE TCMA_WORK_CHECK_LIST SET BLP_CHECK = 'Y', UPDATE_DTTM = CURRENT_TIMESTAMP WHERE BLP_CHECK = 'N' AND :checkTime BETWEEN START_TIME AND END_TIME")
    fun updateBlpCheck(checkTime: String): Int

    @Query("UPDATE TCMA_WORK_CHECK_LIST SET ECG_CHECK = 'Y', UPDATE_DTTM = CURRENT_TIMESTAMP WHERE ECG_CHECK = 'N' AND :checkTime BETWEEN START_TIME AND END_TIME")
    fun updateEcgCheck(checkTime: String): Int

    @Query("UPDATE TCMA_WORK_CHECK_LIST SET HTR_CHECK = 'Y', UPDATE_DTTM = CURRENT_TIMESTAMP WHERE HTR_CHECK = 'N' AND :checkTime BETWEEN START_TIME AND END_TIME")
    fun updateHtrCheck(checkTime: String): Int

    @Query("UPDATE TCMA_WORK_CHECK_LIST SET OXS_CHECK = 'Y', UPDATE_DTTM = CURRENT_TIMESTAMP WHERE OXS_CHECK = 'N' AND :checkTime BETWEEN START_TIME AND END_TIME")
    fun updateOxsCheck(checkTime: String): Int

    @Query("DELETE FROM TCMA_WORK_CHECK_LIST WHERE WORK_ID = :workId")
    fun deleteOne(workId: String)

}