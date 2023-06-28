package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.api.res.ResAp
import com.familidata.sbnwas_cma.api.res.ResDeviceConfig
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoNotification
import com.familidata.sbnwas_cma.base.model.vo.VoWorkMonitor
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.entity.Ap
import com.familidata.sbnwas_cma.room.entity.CmaNotice
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CmaNoticeAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = CmaNoticeAc::class.java.name

    fun insert(row: CmaNotice) {
        db.cmaNoticeDao().insert(row)
    }

    fun resetAll() {
        return db.cmaNoticeDao().delete()
    }

    fun selectForList(type:String): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val currentDateTime = LocalDateTime.now()
        var monthago = currentDateTime.minusDays(30)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val workDate = currentDateTime.format(dateFormat)
        val monthAgoDate = monthago.format(dateFormat)
        val origialList = db.cmaNoticeDao().selectForList(type,workDate, monthAgoDate)
        var date = ""
        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.CREATE_DTTM!!.split(" ")[0]) {
                date = temp.CREATE_DTTM!!.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.CREATE_DTTM!!))
            }
            listofVo.add(
                VoNotification(temp),
            )
            ++i
        }
        return listofVo
    }
}