package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioCho
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioCho
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioChoAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioCho) {
        db.bioChoDao().insert(row)
    }

    fun countADay(userId: String, busType: String): Int {
        return db.bioChoDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId, busType.split("_")[1])
    }

//    fun selectByDate(start: String, end: String, userId: String): MutableList<PBean> {
//        fun getString(entitiy: BioCho): String {
//            return "측정 일시 : " + entitiy.GET_TIME + "\n" + "수축기 : " + entitiy.SYSTOLIC + "\n" + "이완기 : " + entitiy.DIASTOLIC + "\n" + "전송 여부 : " + entitiy.SAVE_TO_SERVER_YN
//        }
//
//        val listofVo = mutableListOf<PBean>()
//        Log.i("SIZE OF LIST", start, end)
//        for (temp in db.bioChoDao().selectByDate(start, end, userId)) {
//            listofVo.add(VoBioCommon(getString(temp)))
//            Log.i("-", getString(temp))
//        }
//        return listofVo
//    }

    fun selectForMonitor(busType: String): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioChoDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID), busType.split("_")[1])
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
                    first = temp.CHO_VALUE!!,
                    second = "mg/DL",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID),
                    busType = busType,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(busType: String): VoBioCho? {
        val entity = db.bioChoDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID), busType.split("_")[1])
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioCho(entity, iconAddr)
        else return null
    }

    fun selectLastOneGetTime(userId: String, deviceId: String, busType: String): String? {
        return try {
            db.bioChoDao().selectLastOneGetTime(userId, deviceId, busType.split("_")[1])
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String, busType: String): Int {
        return db.bioChoDao().countByPeriod(fromDateString, toDateString, userId, busType.split("_")[1])
    }

    fun selectLast(deviceId: String, userId: String, busType: String): VoBioCho? {

        val entity = db.bioChoDao().selectLast(deviceId, userId, busType.split("_")[1])

        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioCho(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        val returnVal = db.bioChoDao().delete()

        return returnVal
    }

    fun getCnt(busType: String): Int {
        return db.bioChoDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID), choType = busType.split("_")[1])
    }

    fun insertOrReplace(item: BioCho) {
        db.bioChoDao().insert(item)
    }

    fun selectForMonitorChart(busType: String): MutableList<BarEntry> {
        val listofVo = mutableListOf<BarEntry>()
        val origialList = db.bioChoDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID), busType.split("_")[1])

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(BarEntry(i * 01f, (temp.CHO_VALUE!!).toFloat()))
            ++i
        }
        return listofVo
    }
}
