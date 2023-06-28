package com.familidata.sbnwas_cma.common.job.common

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.*
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.familidata.sbnwas_cma.util.CommonUtil.toDoubleStringData
import com.familidata.sbnwas_cma.util.CommonUtil.toIntStringData
import com.familidata.sbnwas_cma.util.HealthConnectClientUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HealthConnectorExecutor : IExecutor {

    private var context: Context? = null
    private var healthConnectClient: HealthConnectClient? = null

    private var TAG: String = HealthConnectorExecutor::class.java.name

    private var defaultZoneOffSet: ZoneOffset? = null

    override suspend fun execute(context: Context) {

        this.context = context

        defaultZoneOffSet = OffsetDateTime.now().getOffset()

        // CMA 설정 연계 주기와 동기화
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.SYNC_CMA_SET)
            || !HealthConnectClientUtil.checkHealthConnectClient(context)
        ) {
            return;
        }

        if ("" == DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)) {
            return
        }

        Log.i(TAG, "get health connect client")

        this.healthConnectClient = HealthConnectClientUtil.getHealthConnectClient(context)

        try {

            val now: LocalDateTime = LocalDateTime.now();
            val toDate: LocalDateTime = LocalDateTime.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val fromDate: LocalDateTime = toDate.minusDays(
                if (DBManager.withDB().withProperty().getProperty(PropertyObj.SYNC_SLEEP_SET) == "") 30
                else 15
            )

            // get sleep infomation
            getSleeps(fromDate, toDate)

            // get Distance information
            getDistnaces(fromDate, toDate)

            // get steps information
            getSteps(fromDate, toDate)

            // get Speed information
            getSpeeds(fromDate, toDate)

            // get Activity information
            getActivities(fromDate, toDate)

            // get Total Calories burned information
            getTotalCaloriesBurneds(fromDate, toDate)

            // get Active Calories burned information : TODO
            //getActiveCaloriesBurned(fromDate, toDate)

            // get Oxygen saturation information : TODO
            //getOxygenSaturation(fromDate, toDate)

            // get VO2Max information : TODO
            //getVo2Max(fromDate, toDate)

            DBManager.withDB().withProperty().setProperty(PropertyObj.SYNC_SLEEP_SET, "Y")

            CoroutineScope(Dispatchers.Main).launch {
                CommonApi().sendAllBioLog()
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d(e.printStackTrace())
        }

    }

    private suspend fun getVo2Max(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get vo2max", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                Vo2Max::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        val result = StringBuilder()

        // header & item
        for (vo2Max in response.records) {

            result.append("====> ")
            result.append("vo2max").append(" :: ")

            result.append("method ").append(" :: ").append(vo2Max.measurementMethod).append(" :: ")
            result.append("value ").append(" :: ").append(vo2Max.vo2MillilitersPerMinuteKilogram).append(" :: ")

            result.append(vo2Max.time.atOffset(vo2Max.zoneOffset ?: defaultZoneOffSet).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" :: ")

            result.append(vo2Max.metadata.dataOrigin.packageName).append(" :: ")
            result.append(HealthConnectClientUtil.getDataOriginName(vo2Max.metadata.dataOrigin.packageName))

            Log.i(result.toString())

            result.clear()

        }

        //CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getTotalCaloriesBurneds(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get total calories burned", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                TotalCaloriesBurned::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (item in response.records) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            if (item.energyKcal < 1.0) {
                continue
            }

            DBManager.withDB().withBioCalorie().insert(
                BioCalorie(
                    GET_TIME = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    START_DATE = item.startTime.atOffset(item.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    END_DATE = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    ENERGY_KCAL = toDoubleStringData(item.energyKcal.toString()),
                    SOURCE = item.metadata.dataOrigin.packageName,
                    SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(item.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_CALORIE, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }


    private suspend fun getActiveCaloriesBurned(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get active calories burned", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                ActiveCaloriesBurned::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        val result = StringBuilder()

        // header & item
        for (caloriesBurned in response.records) {

            result.append("====> ")
            result.append("Active 칼로리소모량").append(" :: ")

            result.append(caloriesBurned.energyKcal).append(" kcal").append(" :: ")

            result.append(caloriesBurned.endTime.atOffset(caloriesBurned.endZoneOffset ?: defaultZoneOffSet).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" :: ")

            result.append(caloriesBurned.startTime.atOffset(caloriesBurned.startZoneOffset ?: defaultZoneOffSet)).append(" ~ ").append(caloriesBurned.endTime.atOffset(caloriesBurned.endZoneOffset ?: defaultZoneOffSet)).append(" :: ")

            result.append(caloriesBurned.metadata.dataOrigin.packageName).append(" :: ")
            result.append(HealthConnectClientUtil.getDataOriginName(caloriesBurned.metadata.dataOrigin.packageName))

            Log.i(result.toString())

            result.clear()

        }

        //CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getOxygenSaturation(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get oxygen saturation", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                OxygenSaturation::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        val result = StringBuilder()

        // header & item
        for (oxygenSaturation in response.records) {

            result.append("====> ")
            result.append("산소포화도").append(" :: ")

            result.append(oxygenSaturation.percentage).append(" %").append(" :: ")

            result.append(oxygenSaturation.time.atOffset(oxygenSaturation.zoneOffset ?: defaultZoneOffSet).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" :: ")

            result.append(oxygenSaturation.metadata.dataOrigin.packageName).append(" :: ")
            result.append(HealthConnectClientUtil.getDataOriginName(oxygenSaturation.metadata.dataOrigin.packageName))

            Log.i(result.toString())

            result.clear()

        }

        //CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getSteps(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get steps", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                Steps::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (item in response.records) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            DBManager.withDB().withBioStep().insert(
                BioStep(
                    GET_TIME = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    START_DATE = item.startTime.atOffset(item.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    END_DATE = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    COUNT = toIntStringData(item.count.toString()),
                    SOURCE = item.metadata.dataOrigin.packageName,
                    SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(item.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        // 필요 없음
        //getStepCadences(fromDate, toDate)

        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_STEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getStepCadences(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get step cadences", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                StepsCadenceSeries::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        val result = StringBuilder()

        for (stepCadence in response.records) {

            result.append("====> ")
            result.append("걸음 상세").append(" :: ")
            result.append(stepCadence.samples.size).append(" 상세 수 (")
            result.append(ChronoUnit.MINUTES.between(stepCadence.startTime, stepCadence.endTime)).append(" 분").append(")")
            result.append(stepCadence.startTime.atOffset(stepCadence.startZoneOffset ?: defaultZoneOffSet)).append(" ~ ").append(stepCadence.endTime.atOffset(stepCadence.endZoneOffset ?: defaultZoneOffSet)).append(" :: ")
            result.append(stepCadence.metadata.dataOrigin.packageName).append(" :: ")
            result.append(HealthConnectClientUtil.getDataOriginName(stepCadence.metadata.dataOrigin.packageName)).append("\n")

            for (stepsCadence in stepCadence.samples) {

                result.append("======> 걷기 상세 ").append(" :: ")
                result.append(stepsCadence.rate).append(" ( 분당 걸음 수 )").append(" :: ")
                result.append(stepsCadence.time.atOffset(stepCadence.startZoneOffset ?: defaultZoneOffSet)).append(" \n")

            }

            Log.i(result.toString())

            result.clear()

        }

        //CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getDistnaces(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get distnaces", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                Distance::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (item in response.records) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            if (item.distanceMeters < 1.0) {
                continue
            }

            DBManager.withDB().withBioDistance().insert(
                BioDistance(
                    GET_TIME = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    START_DATE = item.startTime.atOffset(item.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    END_DATE = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DISTANCE_METER = toDoubleStringData(item.distanceMeters.toString()),
                    SOURCE = item.metadata.dataOrigin.packageName,
                    SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(item.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_DISTANCE, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getSpeeds(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get step cadences", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                SpeedSeries::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (item in response.records) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            var metersPerSeconds = 0.0

            for (temp in item.samples) {
                metersPerSeconds += temp.metersPerSecond;
            }

            if (metersPerSeconds < 1.0) {
                continue
            }

            DBManager.withDB().withBioSpeed().insert(
                BioSpeed(
                    GET_TIME = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    START_DATE = item.startTime.atOffset(item.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    END_DATE = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    SPEED_M_SECOND = toDoubleStringData((metersPerSeconds / item.samples.size).toString()),
                    SOURCE = item.metadata.dataOrigin.packageName,
                    SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(item.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        //CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

    private suspend fun getActivities(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get activities", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                ActivitySession::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (item in response.records) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            if (ChronoUnit.MINUTES.between(item.startTime, item.endTime) < 1.0) {
                continue
            }

            DBManager.withDB().withBioActivity().insert(
                BioActivity(
                    GET_TIME = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    START_DATE = item.startTime.atOffset(item.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    END_DATE = item.endTime.atOffset(item.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    PERIOD_MINUTE = toDoubleStringData(ChronoUnit.MINUTES.between(item.startTime, item.endTime).toString()),
                    TYPE = item.activityType,
                    TYPE_TEXT = HealthConnectClientUtil.getActivityTypeText(item.activityType),
                    SOURCE = item.metadata.dataOrigin.packageName,
                    SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(item.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_ACTIVITY, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }


    private suspend fun getSleeps(fromDate: LocalDateTime, toDate: LocalDateTime) {

        Log.i("get sleeps", "start..")

        val response = healthConnectClient!!.readRecords(
            ReadRecordsRequest(
                SleepSession::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(fromDate, toDate)
            )
        )

        for (sleepRecord in response.records) {

            val sleepStageRecords = healthConnectClient!!.readRecords(
                ReadRecordsRequest(
                    SleepStage::class,
                    timeRangeFilter = TimeRangeFilter.between(sleepRecord.startTime, sleepRecord.endTime)
                )
            )

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            var nowString = Instant.now().atOffset(sleepRecord.endZoneOffset ?: defaultZoneOffSet).format(formatter)

            var lightSum = 0L
            var remSum = 0L
            var deepSum = 0L
            var etcSum = 0L

            for (sleepStage in sleepStageRecords.records) {

                DBManager.withDB().withBioSleepStage().insert(
                    BioSleepStage(
                        STAGE_ID = sleepStage.startTime.toEpochMilli(),
                        SESSION_ID = sleepRecord.startTime.toEpochMilli(),
                        USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                        STAGE_CODE = sleepStage.stage,
                        STAGE_TEXT = HealthConnectClientUtil.getSleepStageText(sleepStage.stage),
                        STAGE_START_DATE = sleepStage.startTime.atOffset(sleepStage.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                        STAGE_END_DATE = sleepStage.endTime.atOffset(sleepStage.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                        STAGE_PERIOD = ChronoUnit.MINUTES.between(sleepStage.startTime, sleepStage.endTime),
                        CREATE_DTTM = nowString,
                        SAVE_TO_SERVER_YN = "N",
                    )
                )

                when (sleepStage.stage) {
                    "light" -> lightSum += ChronoUnit.MINUTES.between(sleepStage.startTime, sleepStage.endTime)
                    "deep" -> deepSum += ChronoUnit.MINUTES.between(sleepStage.startTime, sleepStage.endTime)
                    "rem" -> remSum += ChronoUnit.MINUTES.between(sleepStage.startTime, sleepStage.endTime)
                    else -> etcSum += ChronoUnit.MINUTES.between(sleepStage.startTime, sleepStage.endTime)
                }

            }

            DBManager.withDB().withBioSleepSession().insert(
                BioSleepSession(
                    SESSION_ID = sleepRecord.startTime.toEpochMilli(),
                    DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
                    USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                    GET_TIME = sleepRecord.endTime.atOffset(sleepRecord.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    SESSION_START_DATE = sleepRecord.startTime.atOffset(sleepRecord.startZoneOffset ?: defaultZoneOffSet).format(formatter),
                    SESSION_END_DATE = sleepRecord.endTime.atOffset(sleepRecord.endZoneOffset ?: defaultZoneOffSet).format(formatter),
                    SESSION_PERIOD = ChronoUnit.MINUTES.between(sleepRecord.startTime, sleepRecord.endTime),
                    SESSION_DEEP = deepSum,
                    SESSION_LIGHT = lightSum,
                    SESSION_REM = remSum,
                    SESSION_ETC = etcSum,
                    SESSION_SOURCE = sleepRecord.metadata.dataOrigin.packageName,
                    SESSION_SOURCE_TEXT = HealthConnectClientUtil.getDataOriginName(sleepRecord.metadata.dataOrigin.packageName),
                    DEVICE_SERIAL = "",
                    CREATE_DTTM = nowString,
                    SAVE_TO_SERVER_YN = "N",
                )
            )

        }

        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_SLEEP, DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))

    }

}