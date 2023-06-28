package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioBlp
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBas
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioBlpAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioBlp) {
        db.bioBlpDao().insert(row)
    }

    fun countADay(userId: String): Int {
        return db.bioBlpDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun createBlpMessageDatas(topic: String, formattedNow: String, item: JSONObject) {

        var getTime: String = item.getString("get_time")

        val temp = BioBlp(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            SYSTOLIC = CommonUtil.toIntStringData(item.getString("systolic")), // 혈압은 정수로
            DIASTOLIC = CommonUtil.toIntStringData(item.getString("diastolic")),    // 혈압은 정수로
            HR = "0", //  watch 혈압계는 해당 없음
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "N",
        )

        insert(temp)

        // update work check list
        DBManager.withDB().withWorkCheckList().updateBioCheck(topic, getTime)

    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioBlpDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var date = ""
        var i = 1
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.GET_TIME.split(" ")[0]) {
                date = temp.GET_TIME.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.GET_TIME))
            }
            listofVo.add(
                VoBioMonitorData(
                    time = temp.GET_TIME,
                    first = temp.SYSTOLIC!!.split(".")[0] + " / " + temp.DIASTOLIC!!.split(".")[0],
                    second = "mmHg",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_BLP,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioBlp? {
        val entity = db.bioBlpDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBlp(entity, iconAddr)
        else return null
    }

    fun selectLastOneGetTime(userId: String, deviceId: String): String? {
        return try {
            db.bioBlpDao().selectLastOneGetTime(userId, deviceId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioBlpDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectLast(deviceId: String, userId: String): VoBioBlp? {
        val entity = db.bioBlpDao().selectLast(deviceId, userId)

        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBlp(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        val returnVal = db.bioBlpDao().delete()

        return returnVal
    }

    fun getCnt(): Int {
        return db.bioBlpDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioBlp) {
        db.bioBlpDao().insert(item)
    }

    fun selectForMonitorChart(): MutableList<CandleEntry> {
        val listofVo = mutableListOf<CandleEntry>()
        val origialList = db.bioBlpDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(CandleEntry(i * 01f, temp.SYSTOLIC!!.toFloat(), temp.DIASTOLIC!!.toFloat(), temp.SYSTOLIC!!.toFloat(), temp.DIASTOLIC!!.toFloat()))
            ++i
        }
        return listofVo
    }
}
