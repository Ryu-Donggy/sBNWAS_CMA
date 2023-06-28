package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import org.json.JSONObject

class VoBioBlp(val vo: BioBlp, var iconAddr: String) : VoBio() {

    var mainDate = ""
    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_BLP
        vo.SYSTOLIC?.let { this.first = it.split(".")[0] }
        vo.DIASTOLIC?.let { this.second = it.split(".")[0] }
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}