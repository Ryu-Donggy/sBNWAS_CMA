package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoBioCommon
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioAcc
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BioAccAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioAccAc::class.java.name

    fun insert(row: BioAcc) {
        db.bioAccDao().insert(row)
    }

    /**
     * 1. 가속도 데이터 parsing
     */
    fun createAccMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioAcc(
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
        return db.bioAccDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun reSetAll() {
        return db.bioAccDao().delete()

    }


}
