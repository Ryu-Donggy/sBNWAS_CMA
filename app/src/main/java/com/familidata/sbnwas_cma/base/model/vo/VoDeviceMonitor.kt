package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.api.res.CmsConfig
import com.familidata.sbnwas_cma.api.res.ResponseUserInfoData
import com.familidata.sbnwas_cma.api.res.UserInfoDetail
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity

open class VoDeviceMonitor(val data: DeviceConfig, backgroundRes: Int) : PBean() {

    init {
        this.backImage = backgroundRes
        this.viewType = TYPE_A
    }


}