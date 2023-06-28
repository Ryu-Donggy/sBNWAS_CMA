package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoBioSleepHeader
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioSleepSession
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry

class BioSleepSessionAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioSleepSessionAc::class.java.name

    fun insert(row: BioSleepSession) {
        db.bioSleepSessionDao().insert(row)
    }

    fun getCnt(): Int {
        return db.bioSleepSessionDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioSleepSessionDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var date = ""
        var i = 1
        while (i < origialList.size) {
            val temp = origialList[i]
            //if (date != CommonUtil.convertLongToyyyyMMdd_E_(temp.SESSION_END_DATE)) {
            if (date != temp.SESSION_END_DATE) {
                //date = CommonUtil.convertLongToyyyyMMdd_E_(temp.SESSION_END_DATE)
                date = temp.SESSION_END_DATE!!
                listofVo.add(VoBioMonitorDate(date))
            }
            var bean = VoBioMonitorData(
                time = CommonUtil.convertStringToHHmm(temp.SESSION_START_DATE) + " ~ " + CommonUtil.convertStringToHHmm(temp.SESSION_END_DATE),
//                time = temp.SESSION_START_DATE!!.split(" ")[1] + " ~ " + temp.SESSION_END_DATE!!.split(" ")[1],
                first = CommonUtil.convertLogToHour(temp.SESSION_PERIOD).first,
                second = CmaApplication.context!!.getString(R.string.hour),
                third = CommonUtil.convertLogToHour(temp.SESSION_PERIOD).second,
                fourth = CmaApplication.context!!.getString(R.string.minute),
                iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                busType = RxBusObj.BIO_WATCH_SLEEP,
                entity = origialList[i]
            )

            bean.time = CommonUtil.convertStringToHHmm(temp.SESSION_START_DATE) + " ~ " + CommonUtil.convertStringToHHmm(temp.SESSION_END_DATE)
            listofVo.add(bean)
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioSleepHeader? {
        var reslutHeader: VoBioSleepHeader?
        val entity = db.bioSleepSessionDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        return if (entity != null) {
            reslutHeader = VoBioSleepHeader(entity, iconAddr)
//            reslutHeader.date =  CommonUtil.convertStringToHHmm(entity.SESSION_START_DATE) + " ~ " + CommonUtil.convertStringToHHmm(entity.SESSION_END_DATE)
            reslutHeader
        } else null
    }

    fun selectOneForMonitor(sessionId: Long): VoBioSleepHeader? {
        var reslutHeader: VoBioSleepHeader?
        val entity = db.bioSleepSessionDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID), sessionId)
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        return if (entity != null) {
            reslutHeader = VoBioSleepHeader(entity, iconAddr)
//            reslutHeader.date = CommonUtil.convertStringToHHmm(entity.SESSION_START_DATE) + " ~ " + CommonUtil.convertStringToHHmm(entity.SESSION_END_DATE)
            reslutHeader
        } else null
    }

    fun selectLast(deviceId: String, userId: String): VoBioSleepHeader? {
        val entity = db.bioSleepSessionDao().selectLast(deviceId, userId)
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioSleepHeader(entity, iconAddr)
        else return null
    }

    fun convertMinuteToHour(minute: Int): Double {
        return minute.toDouble() / 60
    }

    fun selectForMonitorChart(): MutableList<BarEntry> {

        var weekago = System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7)
        val listofVo = mutableListOf<BarEntry>()
        val origialList = db.bioSleepSessionDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID), weekago)

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
//            var hour = temp.SESSION_PERIOD!! / 60
//            var min = (temp.SESSION_PERIOD!! % 60).toDouble() / 60
            var hour = CommonUtil.convertLogToHour(temp.SESSION_PERIOD).first
            var min = CommonUtil.convertLogToHour(temp.SESSION_PERIOD).second

            listofVo.add(BarEntry(i * 01f, (hour + "." + addZero(min)).toFloat()))
            ++i
        }
        return listofVo
    }


    fun addZero(digit: String): String {
        if (digit.length == 1) {
            return "0$digit"
        } else return digit
    }

    fun selectForMonitorChart(sessionId: Long): MutableList<PieEntry> {
        val listofVo = mutableListOf<PieEntry>()
        val entity = db.bioSleepSessionDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID), sessionId)

        listofVo.add(PieEntry(entity.SESSION_DEEP!!.toFloat(), CmaApplication.context!!.getString(R.string.deep_sleep)))
        listofVo.add(PieEntry(entity.SESSION_LIGHT!!.toFloat(), CmaApplication.context!!.getString(R.string.non_deep_sleep)))
        listofVo.add(PieEntry(entity.SESSION_REM!!.toFloat(), CmaApplication.context!!.getString(R.string.ram_sleep)))
        listofVo.add(PieEntry(entity.SESSION_ETC!!.toFloat(), CmaApplication.context!!.getString(R.string.etc)))
        return listofVo
    }

    fun insertOrReplace(item: BioSleepSession) {
        db.bioSleepSessionDao().insert(item)
    }
}