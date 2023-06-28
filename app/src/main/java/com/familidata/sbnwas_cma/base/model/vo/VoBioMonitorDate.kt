package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.util.CommonUtil

class VoBioMonitorDate(var date: String) : PBean() {


    init {
        this.date = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        this.viewType = TYPE_DATE
    }
}