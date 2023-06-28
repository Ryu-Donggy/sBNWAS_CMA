package com.familidata.sbnwas_cma.room.dao

import androidx.room.*
import com.familidata.sbnwas_cma.room.entity.WorkPlan


@Dao
interface WorkPlanDao {

    @get:Query("SELECT * FROM TCMA_WORK_PLAN")
    val all: List<WorkPlan>

    @Query("SELECT COUNT(*) from TCMA_WORK_PLAN")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg row: WorkPlan)

    @Query("DELETE FROM TCMA_WORK_PLAN")
    fun delete()

    @Query("DELETE FROM TCMA_WORK_PLAN WHERE workId=:workId")
    fun deleteOne(workId: String)

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workStatus='0' ORDER BY workDate ASC")
    fun selectPlans(): List<WorkPlan>

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workStatus='0' AND workDate !=:today ORDER BY workDate ASC")
    fun selectPlans(today: String): List<WorkPlan>

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE date(workDate) >= :fromWorkDate AND date(workDate) <= :endWorkDate")
    fun selectByDate(fromWorkDate: String, endWorkDate: String): List<WorkPlan>

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workStatus == '0' AND ((datetime(:today) BETWEEN datetime(workDate || ' ' || startTime, '-1 hour') AND datetime(workDate || ' ' || endTime)) OR (datetime(:today) <= datetime(workDate || ' ' || startTime))) ORDER BY workDate, startTime LIMIT 1;")
    fun selectToday(today: String): WorkPlan

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workStatus IN ('1', '2') ORDER BY workDate, startTime ASC LIMIT 1")
    fun selectUnFinishedWork(): WorkPlan

    @Query("SELECT COUNT(*) FROM TCMA_WORK_PLAN WHERE  workStatus = '1' ORDER BY workDate, startTime ASC LIMIT 1")
    fun isOnWork(): Int

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workDate =:workDate AND workStatus = '0' AND DATETIME(workDate || ' ' || :timeMinuteString) BETWEEN DATETIME(workDate || ' ' || startTime, '-1 hour') AND DATETIME(workDate || ' ' || startTime)")
    fun getPreWork(workDate: String, timeMinuteString: String): WorkPlan

    @Query("SELECT COUNT(*) FROM TCMA_WORK_PLAN WHERE workDate=:workDate AND (startTime BETWEEN :fromTimeString AND :toTimeString) AND workStatus = '0'")
    fun isOnPreWork(workDate: String, fromTimeString: String, toTimeString: String): Int

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workDate=:workDate AND workStatus = '1'")
    fun getInWork(workDate: String): WorkPlan

    @Update
    fun updateUsers(mWorkPlan: WorkPlan)

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workDate > :workDate ORDER BY workDate, startTime ASC ")
    fun selectPlanForMonitor(workDate: String): List<WorkPlan>

    @Query("SELECT * FROM TCMA_WORK_PLAN WHERE workDate <= :workDate AND workDate >= :monthAgoDate ORDER BY workDate DESC  ")
    fun selectHistoryForMonitor(workDate: String, monthAgoDate: String): List<WorkPlan>
}
