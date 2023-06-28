package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.BioBls
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.github.mikephil.charting.data.Entry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioBlsAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioBlsAc::class.java.name

    fun insert(row: BioBls) {
        db.bioBlsDao().insert(row)
    }


    fun countADay(userId: String): Int {
        return db.bioBlsDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun selectlastData(userId: String): BioBls {

        return db.bioBlsDao().selectLastData(userId)
    }

//    fun selectByDate(start: String, end: String, userId: String): MutableList<PBean> {
//        fun getString(entitiy: BioBls): String {
//            return "측정 일시 : " + entitiy.GET_TIME + "\n" + "Last Seq. : " + entitiy.LAST_SEQ + "\n" + "혈 당 : " + entitiy.GLUCOSEDATA + "\n" + "Eat : " + entitiy.EAT + "\n" + "WHEN_EAT : " + entitiy.WHEN_EAT + "\n" + "ADD TEXT : " + entitiy.ADD_TEXT + "\n" + "기기명 : " + entitiy.MC_NAME + "\n" + "기기 일련번호 : " + entitiy.MC_SERIAL + "\n" + "기기 버전 : " + entitiy.MC_VER + "\n" + "Agency : " + entitiy.AGENCY + "\n" + "IP : " + entitiy.IP + "\n" + "전송 여부 : " + entitiy.SAVE_TO_SERVER_YN
//        }
//
//        val listofVo = mutableListOf<PBean>()
//        Log.i("SIZE OF LIST", start, end)
//        for (temp in db.bioBlsDao().selectByDate(start, end, userId)) {
//            listofVo.add(VoBioCommon(getString(temp)))
//            Log.i("-", getString(temp))
//        }
//        return listofVo
//    }

    fun selectLast(deviceId: String, userId: String): VoBioBls? {
        val entity = db.bioBlsDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }

        if (entity != null)
            return VoBioBls(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        val returnVal = db.bioBlsDao().delete()
        return returnVal
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioBlsDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.BLS_VALUE!!,
                    second = "mg/dL",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_BLS,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioBls? {
        val entity = db.bioBlsDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBls(entity, iconAddr)
        else return null
    }

    fun insertOrReplace(item: BioBls) {
        db.bioBlsDao().insert(item)
    }

    fun selectForMonitorChart(): MutableList<Entry> {
        val listofVo = mutableListOf<Entry>()
        val origialList = db.bioBlsDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            listofVo.add(Entry(i * 01f, temp.BLS_VALUE!!.toFloat()))
            ++i
        }
        return listofVo
    }

}
