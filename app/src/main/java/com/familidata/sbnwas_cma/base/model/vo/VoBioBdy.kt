package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBdy
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import org.json.JSONObject

class VoBioBdy(val vo: BioBdy, var iconAddr: String, viewType: Int) : VoBio() {
    var mainDate = ""

    init {
        this.entity = vo
        this.deviceid = vo.DEVICE_ID + viewType
        this.viewType = viewType
        when (viewType) {

            TYPE_BDY_A -> {  //체중
                vo.WT?.let { first = it }
            }

            TYPE_BDY_B -> {  //체지방량
                vo.BFM?.let { first = it }
            }

            TYPE_BDY_C -> {  //골격근량
                vo.SMM?.let { first = it }
            }

            TYPE_BDY_D -> { //BMI
                vo.BMI?.let { first = it }
            }

            TYPE_BDY_F -> { //체수분
                vo.TBW?.let { first = it }
            }

            TYPE_BDY_G -> { //체지뱡률
                vo.PBF?.let { first = it }
            }

            else -> {  //DESC
                vo.DESCRIPTION?.let { first = it.replace("<BR>", "\n") }
            }
        }
        vo.GET_TIME?.let {
            this.date = it
            if (this.date.length > 19) this.date = this.date.substring(0, 19)
            this.mainDate = CommonUtil.convertLongToyyyyMMdd_E_(CommonUtil.convertDateStringToLong(date))
        }
    }
}