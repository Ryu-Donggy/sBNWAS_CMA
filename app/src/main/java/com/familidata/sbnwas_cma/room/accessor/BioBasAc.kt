package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBas
import com.familidata.sbnwas_cma.rx.RxBusObj
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioBasAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioBasAc::class.java.name

    fun insert(row: BioBas) {
        db.bioBasDao().insert(row)
    }

    fun countADay(userId: String, dataType: String): Int {
        return db.bioBasDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId, dataType)
    }

//    fun selectByDate(start: String, end: String, userId: String, dataType: String): MutableList<PBean> {
//        fun getString(entitiy: BioBas): String {
//            return "측정 일시 : " + entitiy.GET_TIME + "\n" + "심박수 : " + entitiy.BPM + "\n" + "규칙도 : " + entitiy.REQULARITY + "\n" + "\n" + "파일 : " +
//                    entitiy.FTPPATH + "\n" + "장비 : " + entitiy.DEVICE_SERIAL + "\n" + "전송 여부 : " + entitiy.SAVE_TO_SERVER_YN
//        }
//
//        val listofVo = mutableListOf<PBean>()
//        Log.i("selectByDate", start, end)
//        for (temp in db.bioBasDao().selectByDate(start, end, userId, dataType)) {
//            listofVo.add(VoBioCommon(getString(temp)))
//            Log.i("-", getString(temp))
//        }
//        return listofVo
//    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String, dataType: String): Int {
        return db.bioBasDao().countByPeriod(fromDateString, toDateString, userId, dataType)
    }


    fun selectLast(deviceId: String, userId: String, dataType: String): VoBioBas? {
        val entity = db.bioBasDao().selectLast(deviceId, userId, dataType)

        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioBas(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        return db.bioBasDao().delete()
    }

    fun selectForHsmMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioBasDao().selectForHmsMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.BPM!!,
                    second = "bpm",
                    third = temp.REQULARITY!!.split(".")[0],
                    fourth = "%",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_HSM,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneHsmForMonitor(): VoBioBas? {
        val entity = db.bioBasDao().selectOneHmsForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBas(entity, iconAddr)
        else return null
    }


    fun selectForLsmMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioBasDao().selectForLmsMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
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
                    first = temp.POSITION!!.replace("F", CmaApplication.context!!.getString(R.string.bas_front)).replace("B", CmaApplication.context!!.getString(R.string.bas_back)),
                    second = "",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_LSM,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneLsmForMonitor(): VoBioBas? {
        val entity = db.bioBasDao().selectOneLmsForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBas(entity, iconAddr)
        else return null
    }

    fun insertOrReplace(item: BioBas) {
        db.bioBasDao().insert(item)
    }

}
