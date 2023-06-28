package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.room.entity.BioBas

class VoBioBas(val vo: BioBas, var iconAddr: String) : VoBio() {

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID
        vo.DATA_TYPE?.let { type ->


            val front = CmaApplication.context?.getString(R.string.bas_front)
            val back = CmaApplication.context?.getString(R.string.bas_back)

            this.deviceid = this.deviceid + type
            if (type == "H") {
                this.viewType = TYPE_BAS_HSM
                vo.BPM?.let { this.first = it }
                vo.REQULARITY?.let { this.second = it.split(".")[0] }
            } else {
                this.viewType = TYPE_BAS_LSM
                vo.POSITION?.let { this.first = it.replace("F", front!!).replace("B", back!!) }
            }

            vo.GET_TIME?.let {
                this.date = it
                if (this.date.toString().length > 19)
                    this.date = this.date.substring(0, 19)

            }
        }
    }
}