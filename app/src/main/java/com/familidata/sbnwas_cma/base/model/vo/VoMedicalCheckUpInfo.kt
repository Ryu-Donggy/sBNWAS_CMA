package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.api.res.MedicalInfoData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import org.json.JSONObject

class VoMedicalCheckUpInfo(val data: MedicalInfoData) : PBean() {

    init {
        this.viewType = TYPE_ANALY_DATA
    }

}