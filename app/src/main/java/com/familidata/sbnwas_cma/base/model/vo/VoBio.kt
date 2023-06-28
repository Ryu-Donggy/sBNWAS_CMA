package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity

open class VoBio : PBean() {

    var entity: ISuperLogEntity? = null
    var deviceid: String? = null

    var first: String = ""
    var second: String = ""
    var date:String = ""

}