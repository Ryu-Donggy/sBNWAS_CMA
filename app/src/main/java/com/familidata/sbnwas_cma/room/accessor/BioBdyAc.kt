package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioAcc
import com.familidata.sbnwas_cma.room.entity.BioBdy
import com.familidata.sbnwas_cma.room.entity.ISuperLogEntity
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import com.github.mikephil.charting.data.Entry
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BioBdyAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = BioBdyAc::class.java.name

    fun insert(row: BioBdy) {
        db.bioBdyDao().insert(row)
    }

    /**
     * 1. 체성분 데이터 parsing
     * 2. 사용자 체중 update
     */
    fun createBdyMessageDatas(topic: String, formattedNow: String, item: JSONObject) : MutableList<ISuperLogEntity> {

        var getTime: String = item.getString("get_time")
        val list: MutableList<ISuperLogEntity> = ArrayList()

        var data = BioBdy(
            GET_TIME = getTime,
            DEVICE_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.WATCH_ID),
            USER_ID = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            HT = CommonUtil.toDoubleStringData(item.getString("ht")),
            WT = CommonUtil.toDoubleStringData(item.getString("wt")),
            BFM = CommonUtil.toDoubleStringData(item.getString("bfm")),
            SMM = CommonUtil.toDoubleStringData(item.getString("smm")),
            PBF = CommonUtil.toDoubleStringData(item.getString("pbf")),
            BMI = CommonUtil.toDoubleStringData(item.getString("bmi")),
            WHR = "",
            GENDER = DBManager.withDB().withProperty().getProperty(PropertyObj.GENDER),
            VFL = "",
            WT_MIN = "",
            WT_MAX = "",
            SMM_MIN = "",
            SMM_MAX = "",
            BFM_MIN = "",
            BFM_MAX = "",
            VFL_MIN = "",
            VFL_MAX = "",
            IBMI = "",
            BMI_MIN = "",
            BMI_MAX = "",
            IPBF = "",
            PBF_MIN = "",
            PBF_MAX = "",
            IWHR = "",
            WHR_MIN = "",
            WHR_MAX = "",
            BMR_MIN = "",
            BMR_MAX = "",
            WC = "",
            MC = "",
            FC = "",
            FS = "",
            IBFM = "",
            IFFM = "",
            MAX_WT = "",
            MIN_WT = "",
            MAX_SMM = "",
            MIN_SMM = "",
            MAX_BFM = "",
            MIN_BFM = "",
            MAX_BMI = "",
            MIN_BMI = "",
            MAX_PBF = "",
            MIN_PBF = "",
            BMR = CommonUtil.toDoubleStringData(item.getString("bmr")),
            TBW = CommonUtil.toDoubleStringData(item.getString("tbw")),
            DESCRIPTION = "",
            DEVICE_SERIAL = "",
            CREATE_DTTM = formattedNow,
            SAVE_TO_SERVER_YN = "N",
        )
        insert(data)
        DBManager.withDB().withProperty().setProperty(PropertyObj.WEIGHT, data.WT.toString())

        list.add(data)

        return list
    }

    fun countADay(userId: String): Int {
        return db.bioBdyDao().countADay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userId)
    }

    fun selectLast(deviceId: String, userId: String, viewType: Int): VoBioBdy? {
        val entity = db.bioBdyDao().selectLast(deviceId, userId)

        var iconAddr = db.deviceConfigDao().selectIconAddr(deviceId)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null)
            return VoBioBdy(entity, iconAddr, viewType)
        else return null
    }

    fun reSetAll() {
        return db.bioBdyDao().delete()
    }

    fun selectForMonitor(viewType: Int): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        val origialList = db.bioBdyDao().selectForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        var date = ""
        var i = 1
        while (i < origialList.size) {
            val temp = origialList[i]
            if (date != temp.GET_TIME.split(" ")[0]) {
                date = temp.GET_TIME.split(" ")[0]
                listofVo.add(VoBioMonitorDate(temp.GET_TIME))
            }
            listofVo.add(
                VoBioMonitorData(
                    time = temp.GET_TIME,
                    first = temp.let {
                        when (viewType) {
                            PBean.TYPE_BDY_A -> {  //체중
                                temp.WT!!
                            }

                            PBean.TYPE_BDY_B -> {  //체지방량
                                temp.BFM!!
                            }

                            PBean.TYPE_BDY_C -> {  //골격근량
                                temp.SMM!!
                            }

                            PBean.TYPE_BDY_F -> {  //체수분
                                temp.TBW!!
                            }

                            PBean.TYPE_BDY_G -> {  //
                                temp.PBF!!
                            }

                            else -> {  //BMI
                                temp.BMI!!
                            }
                        }
                    },
                    second = temp.let {
                        when (viewType) {
                            PBean.TYPE_BDY_A -> {  //체중
                                "kg"
                            }

                            PBean.TYPE_BDY_B -> {  //체지방량
                                "kg"
                            }

                            PBean.TYPE_BDY_C -> {  //골격근량
                                "kg"
                            }

                            PBean.TYPE_BDY_F -> {  //
                                "kg"
                            }

                            PBean.TYPE_BDY_G -> {  //
                                "%"
                            }

                            else -> {  //BMI
                                "kg / m²"
                            }
                        }
                    },
                    third = "",
                    fourth = "",
                    iconAddr = db.deviceConfigDao().selectIconAddr(temp.DEVICE_ID!!),
                    busType = RxBusObj.BIO_HTR,
                    entity = origialList[i],
                ),
            )
            ++i
        }
        return listofVo
    }

    fun selectOneForMonitor(viewType: Int): VoBioBdy? {
        val entity = db.bioBdyDao().selectOneForMonitor(db.propertyDao().getProperty(PropertyObj.USER_ID))
        if (entity == null) return null
        var iconAddr = db.deviceConfigDao().selectIconAddr(entity.DEVICE_ID!!)
        if (iconAddr == null) {
            iconAddr = ""
        }
        if (entity != null) return VoBioBdy(entity, iconAddr, viewType)
        else return null
    }

    fun insertOrReplace(item: BioBdy) {
        db.bioBdyDao().insert(item)
    }


    fun selectForMonitorChart(viewType: Int): MutableList<Entry> {
//        VoBioBdy
        val listofVo = mutableListOf<Entry>()
        val origialList = db.bioBdyDao().selectForMonitorForChart(db.propertyDao().getProperty(PropertyObj.USER_ID))

        var i = 0
        while (i < origialList.size) {
            val temp = origialList[i]
            when (viewType) {
                PBean.TYPE_BDY_A -> {  //체중
                    listofVo.add(Entry(i * 01f, temp.WT!!.toFloat()))
                }

                PBean.TYPE_BDY_B -> {  //체지방량
                    listofVo.add(Entry(i * 01f, temp.BFM!!.toFloat()))
                }

                PBean.TYPE_BDY_C -> {  //골격근량
                    listofVo.add(Entry(i * 01f, temp.SMM!!.toFloat()))
                }

                PBean.TYPE_BDY_F -> {  //체수분
                    listofVo.add(Entry(i * 01f, temp.TBW!!.toFloat()))
                }

                PBean.TYPE_BDY_G -> {  //체지방률
                    listofVo.add(Entry(i * 01f, temp.PBF!!.toFloat()))
                }

                else -> {  //BMI
                    listofVo.add(Entry(i * 01f, temp.BMI!!.toFloat()))
                }
            }

            ++i
        }
        return listofVo
    }

}