package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.BioOxs
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioOxs(val vo: BioOxs, var iconAddr: String) : VoBio() {
    var mainDate = ""
    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_OXS

        vo.SPO2?.let {
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