package com.familidata.sbnwas_cma

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.rx.RxBus

class CmaApplication : MultiDexApplication() {

    private var bus: RxBus? = null

    override fun onCreate() {
        super.onCreate()
        bus = RxBus()
        context = applicationContext
        instance = this
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "CMA.db").allowMainThreadQueries().fallbackToDestructiveMigration().setJournalMode(RoomDatabase.JournalMode.TRUNCATE).build()
    }

    fun bus(): RxBus? {
        if (bus == null) bus = RxBus()
        return bus
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: CmaApplication
            private set
        lateinit var database: AppDatabase
    }
}