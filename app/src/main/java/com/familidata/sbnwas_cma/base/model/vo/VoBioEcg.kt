package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.BioEcg
import com.familidata.sbnwas_cma.room.entity.BioOxs

class VoBioEcg(val vo: BioEcg, var iconAddr: String) : VoBio() {

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_ECG
        vo.GET_TIME?.let { this.date = it }
    }
}