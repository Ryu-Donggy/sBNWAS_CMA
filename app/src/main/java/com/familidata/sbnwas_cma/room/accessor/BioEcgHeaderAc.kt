package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBls
import com.familidata.sbnwas_cma.room.entity.BioEcg
import com.familidata.sbnwas_cma.room.entity.BioEcgHeader
import com.familidata.sbnwas_cma.rx.RxBusObj
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BioEcgHeaderAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioEcgHeader) {
        db.bioEcgHeaderDao().insert(row)
    }


    fun selectLast(deviceId: String, userId: String): VoBioEcgHeader? {
        val entity = db.bioEcgHeaderDao().selectLast(deviceId, userId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioEcgHeader(entity, iconAddr)
        else return null
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioEcgHeaderDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectAll(deviceId: String, userId: String): MutableList<BioEcgHeader> {

        val list: MutableList<BioEcgHeader> = mutableListOf<BioEcgHeader>()

        for (item in db.bioEcgHeaderDao().selectAll(deviceId, userId)) {
            list.add(item)
        }

        return list
    }

    fun resetAll() {
        return db.bioEcgHeaderDao().delete()
    }

    fun getCnt(): Int {
        return db.bioEcgHeaderDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun selectForMonitor(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioEcgHeaderDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var date = ""
        var i = 1
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.GET_TIME.split(" ")[0]) {
                date = temp.GET_TIME.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.GET_TIME))
            }
            listofVo.add(
                VoBioMonitorData(
                    time = temp.GET_TIME,
                    first = CmaApplication.context!!.getString(R.string.mesure_compelete),
                    second = "",
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_HTR,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(): VoBioEcgHeader? {
        val entity = db.bioEcgHeaderDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioEcgHeader(entity, iconAddr)
        else return null
    }

    fun insertOrReplace(item: BioEcgHeader) {
        db.bioEcgHeaderDao().insert(item)
    }

}