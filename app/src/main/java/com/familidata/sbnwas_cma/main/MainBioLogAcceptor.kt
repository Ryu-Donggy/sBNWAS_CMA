package com.familidata.sbnwas_cma.main

import android.content.Context
import android.content.Intent
import androidx.room.ColumnInfo
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBas
import com.familidata.sbnwas_cma.room.entity.BioBdy
import com.familidata.sbnwas_cma.room.entity.BioBlp
import com.familidata.sbnwas_cma.room.entity.BioBls
import com.familidata.sbnwas_cma.room.entity.BioCho
import com.familidata.sbnwas_cma.room.entity.BioTmm
import com.familidata.sbnwas_cma.room.entity.EntityConvertor
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.familidata.sbnwas_cma.util.CommonUtil.toDoubleStringData
import com.familidata.sbnwas_cma.util.CommonUtil.toIntStringData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainBioLogAcceptor(var context: Context) {

    fun setNewIntent(it: Intent) {
        try {
            var temp: ISuperLogEntity? = null

            val DEVICE_ID = it.getStringExtra("device_id")
//            var USER_ID: String = ""

            val id = it.getStringExtra("id")

//            if (id != "glucose_isense" && id != "blp" && id != "inbody_dial") {
//                USER_ID = it.getStringExtra("user_id")!!
//                Log.i("user id ", USER_ID)
//            }

            if (id == "BLS") {
                Log.i(it.getStringExtra("datas").toString())
                val array = JSONArray(it.getStringExtra("datas"))
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    temp = BioBls(
                        GET_TIME = obj.optString("reg_date"),
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        BLS_VALUE = toIntStringData(obj.optString("bls_value")),   // 혈당은 정수 처리
                        TAG1 = obj.optString("tag1"),
                        TAG2 = toIntStringData(obj.optString("tag2")),   // 식사량은 정수 처리
                        TAG3 = obj.optString("tag3"),
                        DEVICE_SERIAL = obj.optString("device_serial"),
                        CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        SAVE_TO_SERVER_YN = "N"
                    )
                    updateWorkCheckList("bio_bls", obj.optString("reg_date"))
                    DBManager.withDB().withBioBls().insert(temp)
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_1, temp.USER_ID))
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_BLS, DEVICE_ID!!))
                    }
                }
            } else if (id == "BLP") {
                Log.i(it.getStringExtra("datas").toString())
                val array = JSONArray(it.getStringExtra("datas"))
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    temp = BioBlp(
                        GET_TIME = obj.optString("reg_date"),
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        SYSTOLIC = toIntStringData(obj.optString("SBP")!!),  // 혈압은 정수 처리
                        DIASTOLIC = toIntStringData(obj.optString("DBP")!!), // 혈압은 정수 처리
                        HR = toIntStringData(obj.optString("HR")!!), // 심박수는 정수 처리
                        DEVICE_SERIAL = obj.optString("device_serial"),
                        CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        SAVE_TO_SERVER_YN = "N",
                    )
                    DBManager.withDB().withBioBlp().insert(temp)
                    updateWorkCheckList("bio_blp", obj.optString("reg_date"))
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_1, temp.USER_ID))
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_BLP, DEVICE_ID!!))
                    }
                }
            } else if (id == "BDY") {
                Log.i(it.getStringExtra("datas").toString())
                var array = JSONArray(it.getStringExtra("datas")!!)
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    temp = BioBdy(
                        GET_TIME = obj.optString("reg_date"),
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        HT = toDoubleStringData(obj.optString("HT")),   // 신장은 실수 처리(소수점 2자리 반올림)
                        WT = toDoubleStringData(obj.optString("WT")),   // 체중은 실수 처리(소수점 2자리 반올림)
                        BFM = toDoubleStringData(obj.optString("BFM")),   // 체지방량은 실수 처리(소수점 2자리 반올림)
                        SMM = toDoubleStringData(obj.optString("SMM")),   // 골격근량은 실수 처리(소수점 2자리 반올림)
                        PBF = toDoubleStringData(obj.optString("PBF")),   // 체지방률은 실수 처리(소수점 2자리 반올림)
                        BMI = toDoubleStringData(obj.optString("BMI")),   // 신체질량지수는 실수 처리(소수점 2자리 반올림)
                        WHR = toDoubleStringData(obj.optString("WHR")),   // 복부지량률은 실수 처리(소수점 2자리 반올림)
                        GENDER = obj.optString("GENDER"),
                        VFL = toDoubleStringData(obj.optString("VFL")),   // 내장지방수준은 실수 처리(소수점 2자리 반올림)
                        WT_MIN = obj.optString("WT_MIN"),
                        WT_MAX = obj.optString("WT_MAX"),
                        SMM_MIN = obj.optString("SMM_MIN"),
                        SMM_MAX = obj.optString("SMM_MAX"),
                        BFM_MIN = obj.optString("BFM_MIN"),
                        BFM_MAX = obj.optString("BFM_MAX"),
                        VFL_MIN = obj.optString("VFL_MIN"),
                        VFL_MAX = obj.optString("VFL_MAX"),
                        IBMI = toDoubleStringData(obj.optString("IBMI")),   // 체질량지수는 실수 처리(소수점 2자리 반올림)
                        BMI_MIN = obj.optString("BMI_MIN"),
                        BMI_MAX = obj.optString("BMI_MAX"),
                        IPBF = obj.optString("IPBF"),
                        PBF_MIN = obj.optString("PBF_MIN"),
                        PBF_MAX = obj.optString("PBF_MAX"),
                        IWHR = obj.optString("IWHR"),
                        WHR_MIN = obj.optString("WHR_MIN"),
                        WHR_MAX = obj.optString("WHR_MAX"),
                        BMR = obj.optString("BMR"),
                        BMR_MIN = obj.optString("BMR_MIN"),
                        BMR_MAX = obj.optString("BMR_MAX"),
                        WC = obj.optString("WC"),
                        MC = obj.optString("MC"),
                        FC = obj.optString("FC"),
                        FS = obj.optString("FS"),
                        IBFM = obj.optString("IBFM"),
                        IFFM = obj.optString("IFFM"),
                        MAX_WT = obj.optString("MAX_WT"),
                        MIN_WT = obj.optString("MIN_WT"),
                        MAX_SMM = obj.optString("MAX_SMM"),
                        MIN_SMM = obj.optString("MIN_SMM"),
                        MAX_BFM = obj.optString("MAX_BFM"),
                        MIN_BFM = obj.optString("MIN_BFM"),
                        MAX_BMI = obj.optString("MAX_BMI"),
                        MIN_BMI = obj.optString("MIN_BMI"),
                        MAX_PBF = obj.optString("MAX_PBF"),
                        MIN_PBF = obj.optString("MIN_PBF"),
                        TBW = "0",
                        DESCRIPTION = obj.optString("Interpretation"),
                        DEVICE_SERIAL = obj.optString("device_serial"),
                        CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        SAVE_TO_SERVER_YN = "N",
                    )
                    DBManager.withDB().withBioBdy().insert(temp)
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID)) {
                        DBManager.withDB().withProperty().setProperty(PropertyObj.WEIGHT, temp.WT.toString())
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_3, temp.USER_ID))
                    }
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_BDY, DEVICE_ID!!))
                    }
                }
            } else if (id == "TMM") {
                Log.i(it.getStringExtra("datas").toString())
                val array = JSONArray(it.getStringExtra("datas"))
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    temp = BioTmm(
                        GET_TIME = obj.optString("reg_date"),
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        TEMPERATURE = toDoubleStringData(obj.optString("body_temperature")),   // 체온은 실수 처리(소수점 2자리 반올림)
                        DEVICE_SERIAL = obj.optString("device_serial"),
                        CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        SAVE_TO_SERVER_YN = "N"
                    )
                    DBManager.withDB().withBioTmm().insert(temp)
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_1, temp.USER_ID))
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_TMM, DEVICE_ID!!))
                    }
                }
            } else if (id == "skeeper") {
                Log.i(it.getStringExtra("datas").toString())
                val array = JSONArray(it.getStringExtra("datas"))
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    val dataType = if (obj.optString("position").startsWith("H")) "H" else "L"
                    val regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    temp = BioBas(
                        GET_TIME = regDate,
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        DATA_TYPE = dataType,
                        BPM = obj.optString("bpm"),
                        REQULARITY = obj.optString("regularity"),
                        FTPPATH = obj.optString("file_name"),
                        POSITION = obj.optString("position"),
                        DIAGNOSIS = obj.optString("diagnosis"),
                        STATUS = obj.optString("status"),
                        DEVICE_SERIAL = obj.optString("device_serial"),
                        CREATE_DTTM = regDate,
                        SAVE_TO_SERVER_YN = "N"
                    )
                    DBManager.withDB().withBioBas().insert(temp)
                    updateWorkCheckList("bio_bas", regDate)
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_1, temp.USER_ID))
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_BAS, DEVICE_ID!!))
                    }
                }
            } else if (id == "CHO") {
                Log.i(it.getStringExtra("datas").toString())
                val array = JSONArray(it.getStringExtra("datas"))
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    temp = BioCho(
                        GET_TIME = obj.optString("reg_date"),
                        DEVICE_ID = DEVICE_ID!!,
                        USER_ID = obj.optString("user_id"),
                        CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        SAVE_TO_SERVER_YN = "N",
                        CHO_TYPE = obj.optString("cho_type"),
                        CHO_VALUE = obj.optString("cho_value"),
                        DEVICE_SERIAL = obj.optString("device_serial"),
                    )
                    DBManager.withDB().withBioCho().insert(temp)
                    if (temp.USER_ID == DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.MAIN_PAGE_2, temp.USER_ID))
                }
                GlobalScope.launch {
                    launch(Dispatchers.Main) {
                        delay(2000)
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_CHO_TC, DEVICE_ID!!))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_CHO_TG, DEVICE_ID!!))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_CHO_HDL, DEVICE_ID!!))
                        CmaApplication.instance.bus()?.send(Pair(RxBusObj.BIO_CHO_LDL, DEVICE_ID!!))
                    }
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                temp?.let { CommonApi().sendBioLog(EntityConvertor.entitiyToReqData(it)) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateWorkCheckList(topic: String, getTime: String) {
        when (topic) {
            "bio_bas" -> DBManager.withDB().withWorkCheckList().updateBioCheck("BAS", getTime)
            "bio_blp" -> DBManager.withDB().withWorkCheckList().updateBioCheck("BLP", getTime)
            "bio_ecg" -> DBManager.withDB().withWorkCheckList().updateBioCheck("ECG", getTime)
            "bio_htr" -> DBManager.withDB().withWorkCheckList().updateBioCheck("HTR", getTime)
            "bio_oxs" -> DBManager.withDB().withWorkCheckList().updateBioCheck("OXS", getTime)
        }
    }
}