package com.familidata.sbnwas_cma.room.accessor

import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.res.CmsConfig
import com.familidata.sbnwas_cma.api.res.ResAp
import com.familidata.sbnwas_cma.api.res.ResDeviceConfig
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoDevice
import com.familidata.sbnwas_cma.base.model.vo.VoDeviceMonitor
import com.familidata.sbnwas_cma.base.model.vo.VoSettingMonitor
import com.familidata.sbnwas_cma.base.model.vo.VoSingleTitle
import com.familidata.sbnwas_cma.room.AppDatabase
import com.familidata.sbnwas_cma.room.PAccessor
import com.familidata.sbnwas_cma.room.entity.Ap
import com.familidata.sbnwas_cma.room.entity.DeviceConfig
import com.familidata.sbnwas_cma.room.entity.WorkPlan

class DeviceConfigAc(db: AppDatabase) : PAccessor(db) {

    private val TAG = DeviceConfigAc::class.java.name
    fun select(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        for (temp in db.deviceConfigDao().all) {
            listofVo.add(VoDevice(temp))
//            temp.DEVICE_NAME?.let { Log.i("-", it) }
        }
        return listofVo
    }

    fun selectBdy(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        for (temp in db.deviceConfigDao().selectBdy) {
            listofVo.add(VoDevice(temp))
//            temp.DEVICE_NAME?.let { Log.i("-", it) }
        }
        return listofVo
    }

    var typeMap: HashMap<String, Int> = HashMap()
    fun selectForMonitor(): MutableList<PBean> {
        val list = mutableListOf<PBean>()
        var type = ""
        var nextBack = R.drawable.ract_ffffff
        var i = 0
        for (temp in db.deviceConfigDao().all) {
            if (type != temp.DEVICE_TYPE) {
                type = temp.DEVICE_TYPE
                list.add(VoSingleTitle(temp.DEVICE_TYPE_NAME!!))
                nextBack = R.drawable.ract_rounded15_top_ffffff
                if (i != 0) {
                    list[i - 1].backImage = R.drawable.ract_rounded15_bottom_ffffff
                    list[i - 1].bottomLineVisible = false
                }
                i++
            }
            val tempCnt = typeMap[temp.DEVICE_TYPE]
            if (tempCnt == null) {
                typeMap[temp.DEVICE_TYPE] = 1
            } else {
                typeMap[temp.DEVICE_TYPE] = tempCnt + 1
            }
            list.add(VoDeviceMonitor(temp, nextBack))
            nextBack = R.drawable.ract_ffffff
            i++
        }
        if (list.isNotEmpty()) {
            list.last().backImage = R.drawable.ract_rounded15_bottom_ffffff
            list.last().bottomLineVisible = false
        }

        for (temp in typeMap.toList()) {
            if (temp.second == 1) {
                for (temptemp in list) {
                    if (temptemp is VoDeviceMonitor && temptemp.data.DEVICE_TYPE == temp.first) {
                        temptemp.backImage = R.drawable.ract_rounded15_ffffff
                    }
                }
            }
        }
        return list
    }

    fun selectWithoutWatchNApp(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        for (temp in db.deviceConfigDao().all) {
            listofVo.add(VoDevice(temp))
            temp.DEVICE_NAME?.let { Log.i("-", it) }
        }
        return listofVo
    }

    fun selectChoType(): MutableList<PBean> {
        val listofVo = mutableListOf<PBean>()
        for (temp in db.deviceConfigDao().allCho) {
            listofVo.add(VoDevice(temp))
            temp.DEVICE_NAME?.let { Log.i("-", it) }
        }
        return listofVo
    }

    fun selectWatchID(): String? {
        return db.deviceConfigDao().selectWatch()
    }

    fun insert(row: ResDeviceConfig) {
        db.deviceConfigDao().insert(
            DeviceConfig(
                DEVICE_ID = row.deviceId,
                DEVICE_ORDER = row.deviceOrder,
                DEVICE_TYPE = row.deviceType,
                DEVICE_TYPE_NAME = row.deviceTypeName,
                DEVICE_NAME = row.deviceName,
                DEVICE_MODEL = row.deviceModel,
                DEVICE_VERSION = row.deviceVersion,
                DEVICE_COMPANY = row.deviceCompany,
                APP_PACKAGE = row.appPackage,
                APP_ACTIVITY = row.appActivity,
                APP_ICON = row.appIcon,
            )
        )
    }

    fun resetAll() {
        return db.deviceConfigDao().delete()
    }
}