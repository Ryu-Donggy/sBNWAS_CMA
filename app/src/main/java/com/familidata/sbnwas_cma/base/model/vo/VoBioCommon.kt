package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.WorkPlan

class VoBioCommon(val txt1: String?) : PBean() {


    init {
        this.viewType = TYPE_BIO
    }
}