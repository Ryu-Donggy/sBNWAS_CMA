package com.familidata.sbnwas_cma.common.job.common

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.BloodPressure
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.common.IExecutor
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.EntityConvertor
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil.toIntStringData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.round

class BlpExecutor : IExecutor {
    private var context: Context? = null
    override suspend fun execute(context: Context) {
        if (!DBManager.withDB().withProperty().isOnSchedule(PropertyObj.RETRY_MIN_BLP)) {
            return
        }

        if ("" == DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)) {
            return
        }


        Log.i("혈압 집행자  START")
        this.context = context

        var json: JSONObject?

        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        val nowLocalDateTime: LocalDateTime = LocalDateTime.now()
        val check = DBManager.withDB().withBioBlp().selectLastOneGetTime(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID), DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID))
        val str: String?

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = nowLocalDateTime.format(formatter)
        str = if (check != null) {
            check
        } else {
            nowLocalDateTime.minusDays(7).format(formatter).toString()
        }
        val start: LocalDateTime = LocalDateTime.parse(str, formatter).plusSeconds(1)
        val response = healthConnectClient.readRecords(ReadRecordsRequest(BloodPressure::class, timeRangeFilter = TimeRangeFilter.between(start, nowLocalDateTime)))
        for (data in response.records) {
            json = JSONObject()
            json.put("topic", "bio_blp")
            json.put("memb_id", DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
            json.put("systolic", data.systolicMillimetersOfMercury)
            json.put("diastolic", data.diastolicMillimetersOfMercury)
            json.put(
                "get_time", data.time.atZone(ZoneId.systemDefault()).toLocalDateTime().format(
                    DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS")
                )
            )
            try {

                DBManager.withDB().withBioBlp().createBlpMessageDatas("bio_blp", formatted, json)

                CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_WATCH_BLP,DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID)))
                CoroutineScope(Dispatchers.Main).launch {
                    CommonApi().sendAllBioLog();
                }

            } catch (e: Exception) {
                Log.d(e.printStackTrace())
                e.printStackTrace()
            }
            Log.d("종료시간", nowLocalDateTime.toString())
            Log.d("시작시간", start.toString())
            Log.d(
                "측정일자", data.time.atZone(ZoneId.systemDefault()).toLocalDateTime().format(
                    DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS")
                )
            )
            Log.d("수축기", data.systolicMillimetersOfMercury.toString())
            Log.d("이완기", data.diastolicMillimetersOfMercury.toString())
        }


    }

    private fun updateWorkCheckList(topic: String, getTime: String) {

        when(topic) {
            "bio_bas" -> DBManager.withDB().withWorkCheckList().updateBioCheck("BAS", getTime)
            "bio_blp" -> DBManager.withDB().withWorkCheckList().updateBioCheck("BLP", getTime)
            "bio_ecg" -> DBManager.withDB().withWorkCheckList().updateBioCheck("ECG", getTime)
            "bio_htr" -> DBManager.withDB().withWorkCheckList().updateBioCheck("HTR", getTime)
            "bio_oxs" -> DBManager.withDB().withWorkCheckList().updateBioCheck("OXS", getTime)
        }

    }

}