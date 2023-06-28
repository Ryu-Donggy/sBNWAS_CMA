package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.res.CmsConfig
import com.familidata.sbnwas_cma.api.res.ResponseUserInfoData
import com.familidata.sbnwas_cma.api.res.UserInfoDetail
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity

open class VoSettingMonitor(val data: CmsConfig, backgroundRes: Int) : PBean() {

    var viewValue: String = ""

    init {
        this.backImage = backgroundRes
        this.viewType = TYPE_A
        viewValue = when (data.optValue) {
            "true" -> {
                CmaApplication.context?.getString(R.string.on)!!
            }
            "false" -> {
                CmaApplication.context?.getString(R.string.off)!!
            }
            else -> {
                when (data.optPeriodType) {
                    "M" -> (data.optValue.toInt() * 15).toString() + CmaApplication.context?.getString(R.string.minute)!!
                    "D" -> ((data.optValue.toInt() * 15) / 1440).toString() + CmaApplication.context?.getString(R.string.day)!!
                    else -> ((data.optValue.toInt() * 15) / 60).toString() + CmaApplication.context?.getString(R.string.hour)!!
                }
            }
        }
    }


}