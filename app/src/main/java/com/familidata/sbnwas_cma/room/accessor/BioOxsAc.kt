package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoBioOxs
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioOxs
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.github.mikephil.charting.data.Entry
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioOxsAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioOxsAc::class.java.name

    fun insert(row: BioOxs) {
        db.bioOxsDao().insert(row)
    }

    /**
     * 1. 산소포화도 데이터 parsing
     * 2. 근무 상태 update
     */
    fun createOxsMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioOxs(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            SPO2 = CommonUtil.toIntStringData(item.getString("spo2")),  // 산소포화도는 정수처리
            HEART_RATE = CommonUtil.toIntStringData(item.getString("heart_rate")),   // 심박수는 정수 처리
            TAG1 = "",
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "N",
        )
        insert(data)
        list.add(data)

        // update work check list
        DBManager.withDB().withWorkCheckList().updateBioCheck(topic, getTime)

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioOxsDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioOxsDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectLast(deviceId: String, userId: String): VoBioOxs? {
        val entity = db.bioOxsDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioOxs(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        return db.bioOxsDao().delete()
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioOxsDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.SPO2!!.split(".")[0],
                    second = "%",
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

    fun selectOneForMonitor(): VoBioOxs? {
        val entity = db.bioOxsDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioOxs(entity, iconAddr)
        else return null
    }

    fun getCnt(): Int {
        return db.bioOxsDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioOxs) {
        db.bioOxsDao().insert(item)
    }

    fun selectForMonitorChart(): MutableList<Entry> {
        val listofVo = mutableListOf<Entry>()
        val origialList = db.bioOxsDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(Entry(i * 01f, temp.SPO2!!.toFloat()))
            ++i
        }
        return listofVo
    }

    fun updateTag(userId: String, getTime: String, tag1: String) {
        db.bioOxsDao().updateTag(userId, getTime, tag1)
    }

}
