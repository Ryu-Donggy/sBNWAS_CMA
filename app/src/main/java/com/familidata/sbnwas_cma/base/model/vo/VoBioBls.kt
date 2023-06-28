package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.room.entity.BioBls

class VoBioBls(val vo: BioBls, var iconAddr: String) : VoBio() {

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_BLS

        vo.BLS_VALUE?.let {
            this.first = it.split(".")[0]
        }

        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
        }
    }
}