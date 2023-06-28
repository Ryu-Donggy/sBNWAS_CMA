package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.CmaNotice
import com.familidata.sbnwas_cma.room.entity.WorkPlan

class VoNotification(val model: CmaNotice) : PBean() {


    val visibleTime: String

    init {
        this.viewType = TYPE_A
        visibleTime = model.CREATE_DTTM!!.split(" ")[1]
    }

}