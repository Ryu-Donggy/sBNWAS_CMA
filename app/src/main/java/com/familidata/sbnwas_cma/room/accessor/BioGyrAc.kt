package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioGyr
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioGyrAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioGyrAc::class.java.name

    fun insert(row: BioGyr) {
        db.bioGyrDao().insert(row)
    }

    /**
     * 1. Gyr 데이터 parsing
     */
    fun createGyrMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioGyr(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            AXISX = item.getString("axisx"),
            AXISY = item.getString("axisy"),
            AXISZ = item.getString("axisz"),
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "X",
        )

        insert(data)

        list.add(data)

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioGyrDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun reSetAll() {
        return db.bioGyrDao().delete()

    }

}
