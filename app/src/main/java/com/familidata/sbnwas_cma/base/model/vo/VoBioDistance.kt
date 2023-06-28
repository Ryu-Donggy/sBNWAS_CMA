package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioDistance
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioDistance(val vo: BioDistance, var iconAddr: String) : VoBio() {

    var mainDate = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_DISTANCE
        vo.DISTANCE_METER?.let { this.first = String.format("%,d", it.toFloat().toInt()) }
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}