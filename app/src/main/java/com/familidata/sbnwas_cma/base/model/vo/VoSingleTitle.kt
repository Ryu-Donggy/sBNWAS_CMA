package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.util.CommonUtil

class VoSingleTitle(title: String) : PBean() {

    val title: String

    init {
        when (title) {
            "COMMON" -> this.title = CmaApplication.context?.getString(R.string.basic_setting)!!
            "PRE_WORK" -> this.title = CmaApplication.context?.getString(R.string.setting_before_work)!!
            "IN_WORK" -> this.title = CmaApplication.context?.getString(R.string.setting_on_work)!!
            else -> this.title = title
        }
        this.viewType = TYPE_TITLE
    }
}