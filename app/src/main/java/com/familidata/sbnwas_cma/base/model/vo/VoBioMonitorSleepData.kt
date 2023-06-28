package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioMonitorSleepData(var time: String, var first: String, var second: String) : PBean() {

    init {
        this.viewType = TYPE_MONOITER_SLEEP_DATA
    }
}