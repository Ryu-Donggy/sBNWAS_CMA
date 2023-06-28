package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.BioEcg
import com.familidata.sbnwas_cma.room.entity.BioEcgHeader
import com.familidata.sbnwas_cma.room.entity.BioOxs

class VoBioEcgHeader(val vo: BioEcgHeader, var iconAddr: String) : VoBio() {

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        this.viewType = TYPE_ECG
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.toString().length > 19)
                this.date = this.date.substring(0, 19)
        }
    }
}