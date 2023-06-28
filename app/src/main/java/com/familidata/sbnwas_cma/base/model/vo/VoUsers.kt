package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import org.json.JSONObject

class VoUsers(val userid: String, val userName: String, val userEmail: String, val age: String, val gender: String, val height: String, val weight: String, val telNo:String) : PBean() {

    init {
        this.viewType = TYPE_A
    }


    fun voToJson(): JSONObject {
        val a = JSONObject()
        try {
            a.putOpt("user_id", userid)
            a.putOpt("user_name", userName)
            a.putOpt("user_email", userEmail)
            a.putOpt("age", age)
            a.putOpt("gender", gender)
            a.putOpt("weight", weight)
            a.putOpt("height", height)
            a.putOpt("telNo", telNo)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return a
    }
}