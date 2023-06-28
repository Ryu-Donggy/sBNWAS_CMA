package com.familidata.sbnwas_cma.room

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.room.accessor.*


class DBManager internal constructor() {
    private val db: AppDatabase = CmaApplication.database

    fun resetAll() {
        passwordEncorrect()
//        withBioBlp().reSetAll()
//        withBioGps().reSetAll()
//        withBioGyr().reSetAll()
//        withBioHsm().reSetAll()
//        withBioHtr().reSetAll()
//        withBioLsm().reSetAll()
//        withBioOxs().reSetAll()
//        withWorkPlan().reSetAll()
    }

    //패스워드 틀림으로 인한 로그아웃 사용자
    fun passwordEncorrect() {
        withProperty().setProperty(PropertyObj.USER_ID, "")
    }


    ///////////Access Points
    fun withProperty(): PropertyAc {
        return PropertyAc(db)
    }

    fun withBioBlp(): BioBlpAc {
        return BioBlpAc(db)
    }

    fun withBioGps(): BioGpsAc {
        return BioGpsAc(db)
    }

    fun withBioGyr(): BioGyrAc {
        return BioGyrAc(db)
    }

    fun withBioHtr(): BioHtrAc {
        return BioHtrAc(db)
    }

    fun withBioOxs(): BioOxsAc {
        return BioOxsAc(db)
    }

    fun withBioEcg(): BioEcgAc {
        return BioEcgAc(db)
    }

    fun withBioEcgHeader(): BioEcgHeaderAc {
        return BioEcgHeaderAc(db)
    }

    fun withWorkPlan(): WorkPlanAc {
        return WorkPlanAc(db)
    }

    fun withInnerLog(): InnerLogAc {
        return InnerLogAc(db)
    }

    fun withAp(): ApAc {
        return ApAc(db)
    }

    fun withBioAcc(): BioAccAc {
        return BioAccAc(db)
    }

    fun withBioBls(): BioBlsAc {
        return BioBlsAc(db)
    }

    fun withBioBdy(): BioBdyAc {
        return BioBdyAc(db)
    }

    fun withBioBas(): BioBasAc {
        return BioBasAc(db)
    }

    fun withDeviceConfig(): DeviceConfigAc {
        return DeviceConfigAc(db)
    }

    fun withCmaNotice(): CmaNoticeAc {
        return CmaNoticeAc(db)
    }

    fun withBioLog(): LogForBioAc {
        return LogForBioAc(db)
    }

    fun withWorkCheckList(): WorkCheckListAc {
        return WorkCheckListAc(db)
    }

    fun withBioSleepSession(): BioSleepSessionAc {
        return BioSleepSessionAc(db)
    }

    fun withBioSleepStage(): BioSleepStageAc {
        return BioSleepStageAc(db)
    }

    fun withBioTmm(): BioTmmAc {
        return BioTmmAc(db)
    }

    fun withBioStep(): BioStepAc {
        return BioStepAc(db)
    }

    fun withBioDistance(): BioDistanceAc {
        return BioDistanceAc(db)
    }

    fun withBioSpeed(): BioSpeedAc {
        return BioSpeedAc(db)
    }

    fun withBioActivity(): BioActivityAc {
        return BioActivityAc(db)
    }

    fun withBioCalorie(): BioCalorieAc {
        return BioCalorieAc(db)
    }
    fun withBioCho(): BioChoAc {
        return BioChoAc(db)
    }
    companion object {
        private lateinit var dbManager: DBManager

        fun withDB(): DBManager {
            synchronized(DBManager::class.java) {
                dbManager = DBManager()
            }
            return dbManager
        }
    }
}
