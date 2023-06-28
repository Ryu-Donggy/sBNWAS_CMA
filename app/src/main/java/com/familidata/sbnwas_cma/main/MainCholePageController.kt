package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.UserIdRequestData
import com.familidata.sbnwas_cma.api.res.ResponseXsmData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.analyze.AnalyzeActivity
import com.familidata.sbnwas_cma.main.bio.BioCommonActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioBas
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainCholePageController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainCholePageFragment.with(this, vModel)
        return frag
    }

    override fun setBusStation() {
        try {
            val station = CmaApplication.instance.bus()?.toObservable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                if (it is Pair<String, String>) {
                    (vModel as InViewModel).busArrived(it.first, it.second, DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
                }
            }
            frag?.let {
                if (station != null) {
                    it.disposeBag.add(station)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    override fun onClicking(view: View, item: PBean) {
        frag?.let { BioCommonActivity.launch(it.requireActivity(), item.viewType) }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            viewModelScope.launch {
                getDataFromRoom(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }


        fun busArrived(bus: String, deviceId: String, userId: String) {
            when (bus) {
                RxBusObj.BIO_CHO_TC -> {
                    DBManager.withDB().withBioCho().selectLast(deviceId, userId, bus)?.let {
                        (frag as MainCholePageFragment).setTc(it)
                    }
                }

                RxBusObj.BIO_CHO_TG -> {
                    DBManager.withDB().withBioCho().selectLast(deviceId, userId, bus)?.let {
                        (frag as MainCholePageFragment).setTg(it)
                    }
                }

                RxBusObj.BIO_CHO_HDL -> {
                    DBManager.withDB().withBioCho().selectLast(deviceId, userId, bus)?.let {
                        (frag as MainCholePageFragment).setHdl(it)
                    }

                }

                RxBusObj.BIO_CHO_LDL -> {
                    DBManager.withDB().withBioCho().selectLast(deviceId, userId, bus)?.let {
                        (frag as MainCholePageFragment).setLdl(it)
                    }
                }
            }
        }

        private fun getDataFromRoom(userId: String) {
            val deviceList = DBManager.withDB().withDeviceConfig().selectChoType()
            for (temp: PBean in deviceList) {
                if (temp is VoDevice) {
                    busArrived(temp.model.DEVICE_TYPE + "_TC", temp.model.DEVICE_ID, userId)
                    busArrived(temp.model.DEVICE_TYPE + "_TG", temp.model.DEVICE_ID, userId)
                    busArrived(temp.model.DEVICE_TYPE + "_HDL", temp.model.DEVICE_ID, userId)
                    busArrived(temp.model.DEVICE_TYPE + "_LDL", temp.model.DEVICE_ID, userId)
                }
            }
        }
    }
}