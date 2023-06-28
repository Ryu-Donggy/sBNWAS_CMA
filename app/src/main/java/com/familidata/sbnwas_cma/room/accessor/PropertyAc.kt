package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.Property


class PropertyAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = PropertyAc::class.java.name

    fun setProperty(key: String, value: String) {
//        AsyncTask.execute {
        val property = Property(key, value)
        db.propertyDao().setProperty(property)
//        }
    }


    fun getProperty(key: String): String {
//        AsyncTask.execute {
        var returnVal = db.propertyDao().getProperty(key)
        if (returnVal == null) returnVal = ""
        return returnVal
//        }
    }

    fun setSchedulerCnt(): Int {
        try {
            val cnt = getProperty(PropertyObj.SCHEDULER_CNT)
            if (cnt == "") {
                setProperty(PropertyObj.SCHEDULER_CNT, "0")
            } else {
                setProperty(PropertyObj.SCHEDULER_CNT, (cnt.toInt() + 1).toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(e.printStackTrace())
        }
        Log.i(getProperty(PropertyObj.SCHEDULER_CNT))
        return (getProperty(PropertyObj.SCHEDULER_CNT)).toInt()
    }


    /*
    프로퍼티를 넘겨서 현재 돌타이밍인지 확인한다.
     */
    fun isOnSchedule(propretyName: String): Boolean {
        var result = 0
        try {
            val scCnt = getProperty(PropertyObj.SCHEDULER_CNT).toInt()
            val cnt = getProperty(propretyName).toInt()
            result = scCnt % cnt
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(e.printStackTrace())
            return false
        }
        return result == 0
    }

    fun reSetAll() {
//        AsyncTask.execute {
        val returnVal = db.propertyDao().delete()

        return returnVal
//        }
    }

}
