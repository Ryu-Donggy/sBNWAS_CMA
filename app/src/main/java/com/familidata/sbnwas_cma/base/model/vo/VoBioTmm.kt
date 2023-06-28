package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioTmm
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioTmm(val vo: BioTmm, var iconAddr: String) : VoBio() {
    var mainDate = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_TMM

        vo.TEMPERATURE?.let {
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