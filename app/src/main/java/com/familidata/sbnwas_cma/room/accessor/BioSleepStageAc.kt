package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.api.res.BioSyncData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorSleepData
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioSleepStage
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.util.CommonUtil
import org.json.JSONArray

class BioSleepStageAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioSleepStageAc::class.java.name

    fun insert(row: BioSleepStage) {
        db.bioSleepStageDao().insert(row)
    }

    fun createSleepStageMessageDatas(data: BioSyncData) {
        insert(
            BioSleepStage(
                STAGE_ID = data.etc2!!.toLong(),
                SESSION_ID = data.etc1!!.toLong(),
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                STAGE_CODE = data.etc3,
                STAGE_TEXT = data.etc4,
                STAGE_START_DATE = data.date1,
                STAGE_END_DATE = data.date2,
                STAGE_PERIOD = CommonUtil.toIntStringData(data.value1!!).toLong(),
                CREATE_DTTM = data.get_time,
                SAVE_TO_SERVER_YN = "Y",
            )
        )

    }

    fun selectForMonitor(sessionID: Long): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioSleepStageDao().selectAtSession(sessionID)
        var i = 0
        while (i < origialList.size) {
            var time = CommonUtil.convertStringToHHmm(origialList[i].STAGE_START_DATE) + " ~ " + CommonUtil.convertStringToHHmm(origialList[i].STAGE_END_DATE)
//            var time = origialList[i].STAGE_START_DATE + " ~ " + origialList[i].STAGE_END_DATE
            var first = origialList[i].STAGE_TEXT
            var second = CommonUtil.convertLogToHourDetail(origialList[i].STAGE_PERIOD).first + " " + CommonUtil.convertLogToHourDetail(origialList[i].STAGE_PERIOD).second
            var bean = VoBioMonitorSleepData(
                time = time,
                first = first!!,
                second = second,
            )
            listofVo.add(bean)
            ++i
        }
        return listofVo
    }

    fun selectStageCount(sessionID: Long): Int {
        return db.bioSleepStageDao().count(sessionID)
    }

    fun insertOrReplace(item: BioSleepStage) {
        db.bioSleepStageDao().insert(item)
    }


}