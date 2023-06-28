package com.familidata.sbnwas_cma.room.accessor

import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor


class InnerLogAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = InnerLogAc::class.java.name

    fun insert(txt: String) {
        db.apDao()
        db.innerLogDao().insert(txt)

    }

    fun reSetAll() {
        return db.bioGyrDao().delete()

    }

}
