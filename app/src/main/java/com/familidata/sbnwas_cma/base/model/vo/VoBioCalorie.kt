package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioCalorie
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioCalorie(val vo: BioCalorie, var iconAddr: String) : VoBio() {

    var mainDate = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_CALORIE
        vo.ENERGY_KCAL?.let { this.first = it }
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}