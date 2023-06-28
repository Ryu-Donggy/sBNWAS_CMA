package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioBlp
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.base.model.vo.VoBioEcg
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioEcg
import com.familidata.sbnwas_cma.room.entity.BioEcgHeader
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioEcgAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioEcg) {
        db.bioEcgDao().insert(row)
    }

    /**
     * 1. Ecg 데이터 parsing
     * 2. 근무 상태 update
     */
    fun createEcgMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioEcg(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            ECG_INDEX = item.getString("index"),
            ECG_DATA = item.getJSONArray("datas").toString(),
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "N",
        )

        insert(data)

        list.add(data)

        DBManager.withDB().withBioEcgHeader().insert(
            BioEcgHeader(
                GET_TIME = getTime,
                DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                DEVICE_SERIAL = "",
                CREATE_DTTM = formattedNow,
            )
        )

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioEcgDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun countByPeriod(fromDateString: String, toDateString: String, userId: String): Int {
        return db.bioEcgDao().countByPeriod(fromDateString, toDateString, userId)
    }

    fun selectLast(deviceId: String, userId: String): VoBioEcg? {
        val entity = db.bioEcgDao().selectLast(deviceId, userId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioEcg(entity, iconAddr)
        else return null
    }

    fun reSetAll() {
        val returnVal = db.bioEcgDao().delete()
        return returnVal
    }

    fun getCnt(): Int {
        return db.bioEcgDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioEcg) {
        db.bioEcgDao().insert(item)
    }

}
