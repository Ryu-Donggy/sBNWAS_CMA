package com.familidata.sbnwas_cma.util

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R

object HealthConnectClientUtil {

    private var TAG: String = "HealthConnectClientUtil"

    fun checkHealthConnectClient(context: Context): Boolean {

        if (HealthConnectClient.isAvailable(context)) {
            Log.i(TAG, "Health Connect Client is installed")
            val healthConnectClient = HealthConnectClient.getOrCreate(context)
            return true
        } else {
            Log.i(TAG, "Health Connect Client is not installed")
            return false
        }

    }

    fun getHealthConnectClient(context: Context): HealthConnectClient {
        return HealthConnectClient.getOrCreate(context)
    }

    fun getSleepStageText(sleepStage: String): String {
        return when (sleepStage) {
            "unknown" -> CmaApplication.context!!.getString(R.string.unknown)
            "awake" -> CmaApplication.context!!.getString(R.string.awake)
            "sleeping" -> CmaApplication.context!!.getString(R.string.sleeping)
            "light" -> CmaApplication.context!!.getString(R.string.light)
            "deep" -> CmaApplication.context!!.getString(R.string.deep)
            "rem" -> CmaApplication.context!!.getString(R.string.rem)
            "out_of_bed" -> CmaApplication.context!!.getString(R.string.out_of_bed)
            else -> CmaApplication.context!!.getString(R.string.unknown)
        }
    }

    fun getDataOriginName(packageName: String): String {
        return when (packageName) {
            "com.sec.android.app.shealth" -> CmaApplication.context!!.getString(R.string.com_sec_android_app_shealth)
            else -> CmaApplication.context!!.getString(R.string.where_unknown)
        }
    }

    fun getActivityTypeText(activityType: String): String {
        return when (activityType) {
            "badminton" -> CmaApplication.context!!.getString(R.string.badminton)
            "baseball" -> CmaApplication.context!!.getString(R.string.baseball)
            "basketball" -> CmaApplication.context!!.getString(R.string.basketball)
            "biking" -> CmaApplication.context!!.getString(R.string.biking)
            "boxing" -> CmaApplication.context!!.getString(R.string.boxing)
            "cricket" -> CmaApplication.context!!.getString(R.string.cricket)
            "dancing" -> CmaApplication.context!!.getString(R.string.dancing)
            "fencing" -> CmaApplication.context!!.getString(R.string.fencing)
            "football_american" -> CmaApplication.context!!.getString(R.string.football_american)
            "golf" -> CmaApplication.context!!.getString(R.string.golf)
            "handball" -> CmaApplication.context!!.getString(R.string.handball)
            "hiking" -> CmaApplication.context!!.getString(R.string.hiking)
            "ice_hockey" -> CmaApplication.context!!.getString(R.string.ice_hockey)
            "ice_skating" -> CmaApplication.context!!.getString(R.string.ice_skating)
            "pilates" -> CmaApplication.context!!.getString(R.string.pilates)
            "rowing" -> CmaApplication.context!!.getString(R.string.rowing)
            "rugby" -> CmaApplication.context!!.getString(R.string.rugby)
            "running" -> CmaApplication.context!!.getString(R.string.running)
            "scuba_diving" -> CmaApplication.context!!.getString(R.string.scuba_diving)
            "skating" -> CmaApplication.context!!.getString(R.string.skating)
            "skiing" -> CmaApplication.context!!.getString(R.string.skiing)
            "soccer" -> CmaApplication.context!!.getString(R.string.soccer)
            "softball" -> CmaApplication.context!!.getString(R.string.softball)
            "squash" -> CmaApplication.context!!.getString(R.string.squash)
            "surfing" -> CmaApplication.context!!.getString(R.string.surfing)
            "swimming_open_water" -> CmaApplication.context!!.getString(R.string.swimming_open_water)
            "swimming_pool" -> CmaApplication.context!!.getString(R.string.swimming_pool)
            "table_tennis" -> CmaApplication.context!!.getString(R.string.table_tennis)
            "tennis" -> CmaApplication.context!!.getString(R.string.tennis)
            "volleyball" -> CmaApplication.context!!.getString(R.string.volleyball)
            "walking" -> CmaApplication.context!!.getString(R.string.walking)
            "yoga" -> CmaApplication.context!!.getString(R.string.yoga)
            else -> CmaApplication.context!!.getString(R.string.etc)
        }
    }
}