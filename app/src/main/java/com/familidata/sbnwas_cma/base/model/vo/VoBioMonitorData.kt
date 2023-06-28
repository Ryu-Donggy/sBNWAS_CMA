package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioMonitorData(time: String, var first: String, var second: String, var third: String, var fourth: String, var iconAddr: String, var busType:String, val entity: ISuperLogEntity) : PBean() {

    var time = ""
    var tempFirst = ""

    init {
        this.time = CommonUtil.getTimeStringWithAmPm(time.split(" ")[1].split(".")[0])
        this.viewType = TYPE_MONOITER_DATA
    }
}