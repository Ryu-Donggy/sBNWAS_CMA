package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioTmm
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.github.mikephil.charting.data.Entry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioTmmAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioTmmAc::class.java.name

    fun insert(row: BioTmm) {
        db.bioTmmDao().insert(row)
    }

    fun countADay(userId: String): Int {
        return db.bioTmmDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

//    fun selectByDate(start: String, end: String, userId: String): MutableList<PBean> {
//        fun getString(entitiy: BioTmm): String {
//            return "측정 일시 : " + entitiy.GET_TIME + "\n" + "전송 여부 : " + entitiy.SAVE_TO_SERVER_YN
//        }
//
//        val listofVo = mutableListOf<PBean>()
//        Log.i("selectByDate", start, end)
//        for (temp in db.bioTmmDao().selectByDate(start, end, userId)) {
//            listofVo.add(VoBioCommon(getString(temp)))
//            Log.i("-", getString(temp))
//        }
//        return listofVo
//    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioTmmDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectLast(deviceId: String, userId: String): VoBioTmm? {
        val entity = db.bioTmmDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioTmm(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        return db.bioTmmDao().delete()
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioTmmDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.TEMPERATURE!!,
                    second = "°C",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_TMM,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioTmm? {
        val entity = db.bioTmmDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioTmm(entity, iconAddr)
        else return null
    }

    fun getCnt(): Int {
        return db.bioTmmDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioTmm) {
        db.bioTmmDao().insert(item)
    }

    fun selectForMonitorChart(): MutableList<Entry> {
        val listofVo = mutableListOf<Entry>()
        val origialList = db.bioTmmDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            Log.i(formatFloatToTwoDecimalPlaces(temp.TEMPERATURE!!.toFloat()))
            listofVo.add(Entry(i * 01f, formatFloatToTwoDecimalPlaces(temp.TEMPERATURE!!.toFloat())))
            ++i
        }
        return listofVo
    }

    private fun formatFloatToTwoDecimalPlaces(value: Float): Float {
        val formattedValue = String.format("%.1f", value)
        return formattedValue.toFloat()
    }
}
