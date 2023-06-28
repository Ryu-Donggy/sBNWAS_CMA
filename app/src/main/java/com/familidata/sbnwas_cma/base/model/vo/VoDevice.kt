package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan

class VoDevice(val model: DeviceConfig) : PBean() {

    var displayName = ""

    var isSelected:Boolean = false
    init {
        this.viewType = TYPE_A
        this.displayName = model.DEVICE_NAME + "\n" + model.DEVICE_COMPANY
    }
}