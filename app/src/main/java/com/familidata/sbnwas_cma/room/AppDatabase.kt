package com.familidata.sbnwas_cma.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.familidata.sbnwas_cma.room.dao.*
import com.familidata.sbnwas_cma.room.entity.*

@Database(
    entities = [
        Property::class,
        WorkPlan::class,
        BioBlp::class,
        BioGps::class,
        BioGyr::class,
        BioHtr::class,
        BioOxs::class,
        Ap::class,
        InnerLog::class,
        BioEcg::class,
        BioEcgHeader::class,
        BioAcc::class,
        BioBls::class,
        BioBdy::class,
        BioBas::class,
        DeviceConfig::class,
        CmaNotice::class,
        WorkCheckList::class,
        BioSleepSession::class,
        BioSleepStage::class,
        BioTmm::class,
        BioActivity::class,
        BioDistance::class,
        BioSpeed::class,
        BioStep::class,
        BioCalorie::class,
        BioCho::class,
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun workPlanDao(): WorkPlanDao
    abstract fun bioBlpDao(): BioBlpDao
    abstract fun bioGpsDao(): BioGpsDao
    abstract fun bioGyrDao(): BioGyrDao
    abstract fun bioHtrDao(): BioHtrDao
    abstract fun bioOxsDao(): BioOxsDao
    abstract fun apDao(): ApDao
    abstract fun innerLogDao(): InnerLogDao
    abstract fun bioEcgDao(): BioEcgDao
    abstract fun bioEcgHeaderDao(): BioEcgHeaderDao
    abstract fun bioAccDao(): BioAccDao
    abstract fun bioBlsDao(): BioBlsDao
    abstract fun bioBdyDao(): BioBdyDao
    abstract fun bioBasDao(): BioBasDao
    abstract fun deviceConfigDao(): DeviceConfigDao
    abstract fun cmaNoticeDao(): CmaNoticeDao
    abstract fun workCheckListDao(): WorkCheckListDao
    abstract fun bioSleepSessionDao(): BioSleepSessionDao
    abstract fun bioSleepStageDao(): BioSleepStageDao
    abstract fun bioTmmDao(): BioTmmDao
    abstract fun bioStepDao(): BioStepDao
    abstract fun bioDistanceDao(): BioDistanceDao
    abstract fun bioSpeedDao(): BioSpeedDao
    abstract fun bioActivityDao(): BioActivityDao
    abstract fun bioCalorieDao(): BioCalorieDao
    abstract fun bioChoDao(): BioChoDao

}
