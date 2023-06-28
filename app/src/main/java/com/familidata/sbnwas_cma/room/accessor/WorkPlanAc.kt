package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.api.res.ResAp
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoWork
import com.familidata.sbnwas_cma.base.model.vo.VoWorkMonitor
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.Ap
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WorkPlanAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = WorkPlanAc::class.java.name

    fun delete() {
        db.workPlanDao().delete()
        db.apDao().delete()
    }

    fun insert(row: com.familidata.sbnwas_cma.api.res.ResWorkPlan) {
        db.workPlanDao().insert(
            WorkPlan(
                workId = row.workId,
                workDate = row.workDate,
                workPlace = row.workPlace,
                workRole = row.workRole,
                startTime = row.startTime,
                endTime = row.endTime,
                workStatus = row.workStatus,
                workStatusText = row.workStatusText,
                workStartTime = "",
                workEndTime = "",
            )
        )
        for (apTemp: ResAp in row.aps) {
            db.apDao().insert(
                Ap(workId = row.workId, macAddress = apTemp.macAddress, accessId = apTemp.accessId, imei = apTemp.imei, apId = apTemp.apId)
            )
        }
    }

    fun deletePlans() {
        var list = db.workPlanDao().selectPlans()
        for (temp: WorkPlan in list) {
            db.workPlanDao().deleteOne(temp.workId)
            db.apDao().deleteOne(temp.workId)
        }
    }

    fun deletePlan(workId: String) {
        db.workPlanDao().deleteOne(workId)
        db.apDao().deleteOne(workId)
    }

    fun getTodayPlan(): WorkPlan {
        val nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return db.workPlanDao().selectToday(nowDate)
    }

    fun getUnFinishedWork(): WorkPlan {
        return db.workPlanDao().selectUnFinishedWork()
    }

    fun isOnWork(): Boolean {
//        val nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val cnt = db.workPlanDao().isOnWork()
        return cnt >= 1
    }

    fun selectByDate(start: String, end: String): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        Log.i("SIZE OF LIST", start, end)
        for (temp in db.workPlanDao().selectByDate(start, end)) {
            listofVo.add(VoWork(temp))
            Log.i("SIZE OF LIST", temp.workId, temp.workDate, temp.workPlace)
        }
        return listofVo
    }

    fun getPlanList(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()

        for (temp in db.workPlanDao().selectPlans(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            listofVo.add(VoWork(temp))
            Log.i("SIZE OF LIST", temp.workId, temp.workDate, temp.workPlace)
        }
        return listofVo
    }

    fun isOnPreWork(): Boolean {

        val currentDateTime = LocalDateTime.now();
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

        val workDate = currentDateTime.format(dateFormat)

        val fromTimeString = currentDateTime.format(timeFormat)
        val toTimeString = currentDateTime.plusHours(1).format(timeFormat)

        return db.workPlanDao().isOnPreWork(workDate, fromTimeString, toTimeString) >= 1

    }

    fun getPreWork(workDate: String, timeMinuteString: String): WorkPlan {
        return db.workPlanDao().getPreWork(workDate, timeMinuteString)
    }

    fun getInWork(workDate: String): WorkPlan {
        return db.workPlanDao().getInWork(workDate)
    }

    fun update(mWorkPlan: WorkPlan?) {
        if (mWorkPlan != null) {
            db.workPlanDao().updateUsers(mWorkPlan)
        }
    }

    fun reSetAll() {
        val returnVal = db.workPlanDao().delete()
        return returnVal
    }

    fun selectPlanForMonotor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val currentDateTime = LocalDateTime.now();
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val workDate = currentDateTime.format(dateFormat)
        val fromTimeString = currentDateTime.format(timeFormat)

        val origialList = db.workPlanDao().selectPlanForMonitor(workDate)
        var date = ""
        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.workDate.split(" ")[0]) {
                date = temp.workDate.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.workDate + " 00:00:00"))
            }
            listofVo.add(
                VoWorkMonitor(temp),
            )
            ++i
        }
        return listofVo
    }

    fun selectHistoryForMonotor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val currentDateTime = LocalDateTime.now()
        var monthago = currentDateTime.minusDays(30)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val workDate = currentDateTime.format(dateFormat)

        val monthAgoDate = monthago.format(dateFormat)

        val origialList = db.workPlanDao().selectHistoryForMonitor(workDate, monthAgoDate)
        var date = ""
        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.workDate.split(" ")[0]) {
                date = temp.workDate.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.workDate + " 00:00:00"))
            }
            listofVo.add(
                VoWorkMonitor(temp),
            )
            ++i
        }
        return listofVo
    }
}
