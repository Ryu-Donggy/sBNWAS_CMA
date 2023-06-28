package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.entity.BioGps


class ApAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = ApAc::class.java.name

    fun isOnWOrk(bssid: String): Boolean {
        return db.apDao().count(bssid) > 0
    }

}
