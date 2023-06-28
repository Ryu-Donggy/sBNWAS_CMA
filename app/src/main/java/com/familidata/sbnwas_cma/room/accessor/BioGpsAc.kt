package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioGps
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioGpsAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioGpsAc::class.java.name

    fun insert(row: BioGps) {
        db.bioGpsDao().insert(row)

    }

    /**
     * 1. Gps 데이터 parsing
     */
    fun createGpsMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioGps(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            LONGITUDE = item.getString("longitude"),
            LATITUDE = item.getString("latitude"),
            ALTITUDE = item.getString("altitude"),
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "N",
        )

        insert(data)

        list.add(data)

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioGpsDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun reSetAll() {
        val returnVal = db.bioGpsDao().delete()

        return returnVal

    }

}
