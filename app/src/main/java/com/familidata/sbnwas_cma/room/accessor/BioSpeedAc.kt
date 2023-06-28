package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioActivity
import com.familidata.sbnwas_cma.room.entity.BioSpeed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BioSpeedAc(db: AppDatabase) : PAccessor(db) {

    fun insert(row: BioSpeed) {
        db.bioSpeedDao().insert(row)
    }

    fun count(userId: String): Int {
        return db.bioSpeedDao().count(userId)
    }

    fun countADay(userId: String): Int {
        return db.bioSpeedDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun reSetAll() {
        return db.bioSpeedDao().delete()
    }

    fun getCnt(): Int {
        return db.bioSpeedDao().count(userId = db.propertyDao().getProperty(PropertyObj.USER_ID))
    }

    fun insertOrReplace(item: BioSpeed) {
        db.bioSpeedDao().insert(item)
    }

}