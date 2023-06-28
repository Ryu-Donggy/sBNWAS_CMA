package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioSleepSession
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioSleepHeader(val vo: BioSleepSession, var iconAddr: String) : VoBio() {

    var period = ""
    var totalSleep = ""
    var deep = ""
    var light = ""
    var rem = ""
    var etc = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_SLEEP
        vo.SESSION_PERIOD?.let {
            first = CommonUtil.convertLogToHour(it).first
            second = CommonUtil.convertLogToHour(it).second
        }

        //this.date = CommonUtil.getTimeStringWithAmPm(vo.SESSION_END_DATE)
        //this.period = CommonUtil.getTimeStringWithAmPm(vo.SESSION_START_DATE) + " ~ " + CommonUtil.getTimeStringWithAmPm(vo.SESSION_END_DATE)
        this.date = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(vo.SESSION_END_DATE.toString()))
        this.period = CommonUtil.convertStringToHHmm(vo.SESSION_START_DATE )+ " ~ " + CommonUtil.convertStringToHHmm(vo.SESSION_END_DATE)

        this.totalSleep = CommonUtil.convertLogToHourDetail(vo.SESSION_PERIOD).first + " " + CommonUtil.convertLogToHourDetail(vo.SESSION_PERIOD).second
        this.deep = CommonUtil.convertLogToHourDetail(vo.SESSION_DEEP).first + " " + CommonUtil.convertLogToHourDetail(vo.SESSION_DEEP).second
        this.light = CommonUtil.convertLogToHourDetail(vo.SESSION_LIGHT).first + " " + CommonUtil.convertLogToHourDetail(vo.SESSION_LIGHT).second
        this.rem = CommonUtil.convertLogToHourDetail(vo.SESSION_REM).first + " " + CommonUtil.convertLogToHourDetail(vo.SESSION_REM).second
        this.etc = CommonUtil.convertLogToHourDetail(vo.SESSION_ETC).first + " " + CommonUtil.convertLogToHourDetail(vo.SESSION_ETC).second
    }
}