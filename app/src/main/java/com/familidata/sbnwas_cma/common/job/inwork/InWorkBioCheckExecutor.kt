package com.familidata.sbnwas_cma.common.job.inwork

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoWorkCheckList
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InWorkBioCheckExecutor : IExecutor {

    private var context: Context? = null

    override suspend fun execute(context: Context) {

        Log.i("InWorkBioCheckExecutor START")
        this.context = context

        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.InWork.WI_RETRY_MIN_ALARM)) {
            Log.i("InWorkBioCheckExecutor END")
            return
        }

        var bios : MutableList<String> = mutableListOf()
        var startTimeString: String = ""
        var endTimeString: String = ""

        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val timeMinuteFormat = DateTimeFormatter.ofPattern("HH:mm")

        val now: LocalDateTime = LocalDateTime.now()

        Log.i("### param : ", now.format(dateFormat))

        // get in work
        val inWork: WorkPlan? = DBManager.withDB().withWorkPlan().getInWork(now.format(dateFormat))

        if (inWork != null) {

            Log.i("### in work : ", inWork.toString())
            val checkList: MutableList<PBean> = DBManager.withDB().withWorkCheckList().selectByWorkId(inWork.workId)

            if (checkList != null) {
                for (temp: PBean in checkList) {
                    if (temp is VoWorkCheckList
                        && temp.item!!.CHECK_TYPE == "1"
                        && (temp.item!!.START_TIME!! <= now.format(dateTimeFormat) && now.format(dateTimeFormat) <= temp.item!!.END_TIME!!)) {

                        if (temp.item!!.BAS_CHECK == "N") bios.add(context.getString(R.string.bas_title))
                        if (temp.item!!.BLP_CHECK == "N") bios.add(context.getString(R.string.blp_title))
                        if (temp.item!!.ECG_CHECK == "N") bios.add(context.getString(R.string.ecg_title))
                        if (temp.item!!.HTR_CHECK == "N") bios.add(context.getString(R.string.htr_title))
                        if (temp.item!!.OXS_CHECK == "N") bios.add(context.getString(R.string.oxs_title))
                        startTimeString = temp.item!!.START_TIME!!
                        endTimeString = temp.item!!.END_TIME!!
                    }
                }
            }

        }

        if (bios.size > 0) {
            val notiTitle = StringBuilder()
            val notiBody = StringBuilder()

            notiTitle.append(context.getString(R.string.noti_on_work_health_check))

            notiBody.append("정해진 시간 [").append(startTimeString).append("~").append(endTimeString).append("] ")
            notiBody.append("내에 ").append(" ").append("[").append(bios.joinToString(", ")).append("]").append(" ").append("항목을 반드시 측정 하세요.")

            CommonUtil.createNotificationChannel(context, notiTitle.toString(), notiBody.toString())
        }

        Log.i("InWorkBioCheckExecutor END")

    }

}