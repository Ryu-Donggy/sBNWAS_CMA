package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioHtr
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.github.mikephil.charting.data.Entry
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioHtrAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioHtrAc::class.java.name

    fun insert(row: BioHtr) {
        db.bioHtrDao().insert(row)
    }

    /**
     * 1. 심박수 데이터 parsing
     * 2. 근무 상태 update
     */
    fun createHtrMessageDatas(topic: String, formattedNow: String, datas: JSONArray) : MutableList<ISuperLogEntity> {

        var getTime: String = ""
        val list: MutableList<ISuperLogEntity> = ArrayList()

        for (i in 0 until datas.length()) {
            val item = datas.getJSONObject(i)
            if (i == 0) getTime = item.getString("get_time")
            var data = BioHtr(
                GET_TIME = item.getString("get_time"),
                DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                HEART_RATE = CommonUtil.toIntStringData(item.getString("heart_rate")),   // 심박수는 정수 처리
                TAG1 = "",
                DEVICE_SERIAL = "",
                CREATE_DTTM = formattedNow,
                SAVE_TO_SERVER_YN = "N",
            )
            insert(data)
            list.add(data)
        } // end for

        // update work check list
        DBManager.withDB().withWorkCheckList().updateBioCheck(topic, getTime)

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioHtrDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioHtrDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectLast(deviceId: String, userId: String): VoBioHtr? {
        val entity = db.bioHtrDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioHtr(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        return db.bioHtrDao().delete()
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioHtrDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.HEART_RATE!!,
                    second = "bpm",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_HTR,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioHtr? {
        val entity = db.bioHtrDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioHtr(entity, iconAddr)
        else return null
    }

    fun getCnt(): Int {
        return db.bioHtrDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioHtr) {
        db.bioHtrDao().insert(item)
    }

    fun selectForMonitorChart(): MutableList<Entry> {
        val listofVo = mutableListOf<Entry>()
        val origialList = db.bioHtrDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(Entry(i * 01f, temp.HEART_RATE!!.toFloat()))
            ++i
        }
        return listofVo
    }

    fun updateTag(userId: String, getTime: String, tag1: String) {
        db.bioHtrDao().updateTag(userId, getTime, tag1)
    }
}
