package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioDistance
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoBioStep
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioDistance
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.github.mikephil.charting.data.BarEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BioDistanceAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioDistance) {
        db.bioDistanceDao().insert(row)
    }

    fun count(userId: String): Int {
        return db.bioDistanceDao().count(userId)
    }

    fun countADay(userId: String): Int {
        return db.bioDistanceDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun reSetAll() {
        return db.bioDistanceDao().delete()
    }

    fun getCnt(): Int {
        return db.bioDistanceDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioDistance) {
        db.bioDistanceDao().insert(item)
    }

    fun selectLast(deviceId: String, userId: String): VoBioDistance? {
        val entity = db.bioDistanceDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioDistance(entity, iconAddr)
        else return null
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioDistanceDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = String.format("%,d", temp.DISTANCE_METER.toFloat().toInt()),
                    second = "m",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID),
                    busType = RxBusObj.BIO_HTR,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioDistance? {
        val entity = db.bioDistanceDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioDistance(entity, iconAddr)
        else return null
    }

    fun selectForMonitorChart(): MutableList<BarEntry> {
        val listofVo = mutableListOf<BarEntry>()
        val origialList = db.bioDistanceDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(BarEntry(i * 01f, (temp.DISTANCE_METER!!).toFloat()))
            ++i
        }
        return listofVo
    }

}