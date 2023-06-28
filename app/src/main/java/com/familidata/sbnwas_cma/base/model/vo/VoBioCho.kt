package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioCho
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioCho(val vo: BioCho, var iconAddr: String) : VoBio() {
    var mainDate = ""

    init {
        this.entity = vo
        vo.CHO_VALUE?.let { first = it }

        vo.CHO_TYPE.let {  //TC,TG,HDL,LDL
            viewType = when (it) {
                "TC" -> {
                    TYPE_CHO_TC
                }

                "TG" -> {
                    TYPE_CHO_TG
                }

                "HDL" -> {
                    TYPE_CHO_HDL
                }

                else -> {
                    TYPE_CHO_LDL
                }
            }
        }
        this.deviceid = vo.DEVICE_ID + viewType
        vo.GET_TIME.let {
            this.date = it
            if (this.date.length > 19) this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}