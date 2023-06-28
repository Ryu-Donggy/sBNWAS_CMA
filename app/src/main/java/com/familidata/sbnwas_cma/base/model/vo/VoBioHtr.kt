package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.BioHtr
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioHtr(val vo: BioHtr, var iconAddr: String) : VoBio() {
    var mainDate = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_HTR

        vo.HEART_RATE?.let {
            this.first = it
        }
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}