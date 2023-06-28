package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.api.res.ResWorkPlan
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoWorkCheckList
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.WorkCheckList
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WorkCheckListAc(db: AppDatabase): PAccessor(db) {

    private val TAG = WorkCheckListAc::class.java.name

    fun deletePlans() {
        var list = db.workPlanDao().selectPlans()
        for (temp: WorkPlan in list) {
            db.workCheckListDao().deleteOne(temp.workId)
        }
    }

    fun deletePlan(workId: String) {
        db.workCheckListDao().deleteOne(workId)
    }

    fun insert(row: ResWorkPlan) {

        val list : MutableList<WorkCheckList> = mutableListOf()

        // create pre work check list
        createPreWorkCheckList(row, list)

        // create on work check list
        createOnWorkCheckList(row, list)

        for (temp in list) {
            insert(temp)
        }

    }

    fun insert(row: WorkCheckList) {
        db.workCheckListDao().insert(row)
    }

    fun countByWorkId(workId: String): Int {
        return db.workCheckListDao().countByWorkId(workId)
    }

    fun selectByWorkId(workId: String): MutableList<PBean> {

        val list = mutableListOf<PBean>()
//        Log.i("selectByWorkId", workId)

        for (temp in db.workCheckListDao().selectByWorkId(workId)) {
            list.add(VoWorkCheckList(temp))
            //Log.i("item in list", temp.WORK_ID, temp.CHECK_TYPE)
        }

        return list

    }



    fun updateBioCheck(bioType: String, getTime: String) {

        val checkDateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val getTimeString: String = LocalDateTime.parse(getTime.substring(0, 16), checkDateTimeFormat).format(checkDateTimeFormat);

        val result = when(bioType) {
            "BAS" -> db.workCheckListDao().updateBasCheck(getTimeString)
            "BLP" -> db.workCheckListDao().updateBlpCheck(getTimeString)
            "ECG" -> db.workCheckListDao().updateEcgCheck(getTimeString)
            "HTR" -> db.workCheckListDao().updateHtrCheck(getTimeString)
            "OXS" -> db.workCheckListDao().updateOxsCheck(getTimeString)
            else -> 0
        }

    }

    private fun createPreWorkCheckList(row: ResWorkPlan, list : MutableList<WorkCheckList>) : Unit {

        //Log.i("@@@ create pre work", row.workId, row.startTime, row.endTime)

        val dateFormat: String = "yyyy-MM-dd"
        val timeFormat: String = "HH:mm:ss"
        val minuteFormat: String = "HH:mm"

        val workStartDateTime: LocalDateTime = LocalDateTime.parse(row.workDate + " " + row.startTime, DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))

        val startString = workStartDateTime.minusHours(1).format(DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))
        val endString = workStartDateTime.format(DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))
        var userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID)
        val nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("$dateFormat $timeFormat"))

        //Log.i("@@@ create pre work param : ", startString, endString, nowString)

        val preBlpCheck: String = if (DBManager.withDB().withProperty().getProperty(PropertyObj.PreWork.WB_BIO_BLP).toBoolean()) {if (checkBlp("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
        val preEcgCheck: String = if (DBManager.withDB().withProperty().getProperty(PropertyObj.PreWork.WB_BIO_ECG).toBoolean()) {if (checkEcg("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
        val preHtrCheck: String = if (DBManager.withDB().withProperty().getProperty(PropertyObj.PreWork.WB_BIO_HTR).toBoolean()) {if (checkHtr("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
        val preOxsCheck: String = if (DBManager.withDB().withProperty().getProperty(PropertyObj.PreWork.WB_BIO_OXS).toBoolean()) {if (checkOxs("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
        val preBasCheck: String = if (DBManager.withDB().withProperty().getProperty(PropertyObj.PreWork.WB_BIO_BAS).toBoolean()) {if (checkBas("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"

        list.add(
            WorkCheckList(
            WORK_ID = row.workId,
            CHECK_ORDER = "1",
            CHECK_TYPE = "0",
            START_TIME = startString,
            END_TIME = endString,
            BAS_CHECK = preBasCheck,
            BLP_CHECK = preBlpCheck,
            ECG_CHECK = preEcgCheck,
            HTR_CHECK = preHtrCheck,
            OXS_CHECK = preOxsCheck,
            UPDATE_DTTM = nowString,
            )
        )

    }

    private fun createOnWorkCheckList(row: ResWorkPlan, list : MutableList<WorkCheckList>) : Unit {

        //Log.i("@@@ create in work", row.workId, row.startTime, row.endTime)

        val dateFormat: String = "yyyy-MM-dd"
        val timeFormat: String = "HH:mm:ss"
        val minuteFormat: String = "HH:mm"

        val workStartDateTime: LocalDateTime = LocalDateTime.parse(row.workDate + " " + row.startTime, DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))
        val workEndDateTime: LocalDateTime = LocalDateTime.parse(row.workDate + " " + row.endTime, DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))
        var userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID)
        val nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("$dateFormat $timeFormat"))
        val stepTime: Int = DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_GET_BIO_HOUR).toInt() * 15 / 60

        //Log.i("@@@ create in work params : ", workStartDateTime, workEndDateTime, stepTime)

        var startDateTime: LocalDateTime = workStartDateTime
        var endDateTime: LocalDateTime = workStartDateTime
        var startString: String = ""
        var endString: String = ""

        var onBasCheck: String = "X"
        var onBlpCheck: String = ""
        var onEcgCheck: String = ""
        var onHtrCheck: String = ""
        var onOxsCheck: String = ""

        var index: Int = 1

        do {

            endDateTime = startDateTime.plusHours(stepTime.toLong())

            if (endDateTime.isAfter(workEndDateTime)) {
                endDateTime = workEndDateTime
            }

            startString = startDateTime.format(DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))
            endString = endDateTime.format(DateTimeFormatter.ofPattern("$dateFormat $minuteFormat"))

            //Log.i("@@@ create in work params 1 : ", startString, endString, stepTime)

            onBlpCheck = if (DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_BLP).toBoolean()) {if (checkBlp("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
            onEcgCheck = if (DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_ECG).toBoolean()) {if (checkEcg("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
            onHtrCheck = if (DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_HTR).toBoolean()) {if (checkHtr("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"
            onOxsCheck = if (DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_OXS).toBoolean()) {if (checkOxs("$startString:00", "$endString:00", userId)) "Y" else "N"} else "X"

            list.add(
                WorkCheckList(
                    WORK_ID = row.workId,
                    CHECK_ORDER = index.toString(),
                    CHECK_TYPE = "1",
                    START_TIME = startString,
                    END_TIME = endString,
                    BAS_CHECK = onBasCheck,
                    BLP_CHECK = onBlpCheck,
                    ECG_CHECK = onEcgCheck,
                    HTR_CHECK = onHtrCheck,
                    OXS_CHECK = onOxsCheck,
                    UPDATE_DTTM = nowString,
                )
            )

            //Log.i("@@@ create in work check params 2 : ", endDateTime, workEndDateTime)

            if (endDateTime.isBefore(workEndDateTime)) {

                //Log.i("@@@ create in work check params 3 : ", endDateTime, workEndDateTime)
                startDateTime = startDateTime.plusHours(stepTime.toLong())

                //Log.i("@@@ create in work check params 4 : ", startDateTime, endDateTime, workEndDateTime)
            }

            index++

        } while ( endDateTime.isBefore(workEndDateTime) )

    }

    private fun checkBlp(fromDateString: String, toDateString: String, userId: String) : Boolean {

        //Log.i("CHECK BLP START")

        var check : Boolean = false;

        val count = DBManager.withDB().withBioBlp().countByPeriod(fromDateString, toDateString, userId)

        if (count == 0) {
            //Log.i("BLP DATA IS NOT EXIST")
        } else {
            //Log.i("BLP DATA EXIST")
            check = true
        }

        return check;

    }


    private fun checkEcg(fromDateString: String, toDateString: String, userId: String) : Boolean {

        //Log.i("CHECK ECG START")

        var check : Boolean = false;

        val count = DBManager.withDB().withBioEcgHeader().countByPeriod(fromDateString, toDateString, userId)

        if (count == 0) {
            //Log.i("ECG DATA IS NOT EXIST")
        } else {
            //Log.i("ECG DATA EXIST")
            check = true
        }

        return check;

    }


    private fun checkHtr(fromDateString: String, toDateString: String, userId: String) : Boolean {

        //Log.i("CHECK HTR START")

        var check : Boolean = false;

        val count = DBManager.withDB().withBioHtr().countByPeriod(fromDateString, toDateString, userId)

        if (count == 0) {
            //Log.i("HTR DATA IS NOT EXIST")
        } else {
            //Log.i("HTR DATA EXIST")
            check = true
        }

        return check;

    }

    private fun checkOxs(fromDateString: String, toDateString: String, userId: String) : Boolean {

        //Log.i("CHECK OXS START")

        var check : Boolean = false;

        val count = DBManager.withDB().withBioOxs().countByPeriod(fromDateString, toDateString, userId)

        if (count == 0) {
            //Log.i("OXS DATA IS NOT EXIST")
        } else {
            //Log.i("OXS DATA EXIST")
            check = true
        }

        return check;

    }

    private fun checkBas(fromDateString: String, toDateString: String, userId: String) : Boolean {

        //Log.i("CHECK BAS START")

        var check : Boolean = false;

        var count = DBManager.withDB().withBioBas().countByPeriod(fromDateString, toDateString, userId, "H")

        if (count == 0) {
            count += DBManager.withDB().withBioBas().countByPeriod(fromDateString, toDateString, userId, "L")
        }

        if (count == 0) {
            //Log.i("OXS DATA IS NOT EXIST")
        } else {
            //Log.i("OXS DATA EXIST")
            check = true
        }

        return check;

    }


}