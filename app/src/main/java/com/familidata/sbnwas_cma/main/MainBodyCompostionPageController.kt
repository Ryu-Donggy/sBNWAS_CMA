package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.bio.BioCommonActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.rx.RxBusObj
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*

class MainBodyCompostionPageController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainBodyCompostionPageFragment.with(this, vModel)
        return frag
    }

    override fun setBusStation() {
        try {
            val station = CmaApplication.instance.bus()?.toObservable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                if (it is Pair<String, String>) {
                    (vModel as InViewModel).busArrivedInit(it.first, it.second, DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
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

    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.clWeight -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_A) }
            }

            R.id.clBFM -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_B) }
            }

            R.id.clBo -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_C) }
            }

            R.id.clBMI -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_D) }
            }

            R.id.clBodyWater -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_F) }
            }

            R.id.clBFMPropotion -> {
                frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BDY_G) }
            }
        }
    }

    override fun onClicking(view: View, item: PBean) {
        if (item is VoDevice) {
            frag?.let { (it as MainBodyCompostionPageFragment).deviceSelect(item) }
            (vModel as InViewModel).getDataFromRoom(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID), item.model.DEVICE_ID)
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            viewModelScope.launch {
                var list = DBManager.withDB().withDeviceConfig().selectBdy()
                if (list.isNotEmpty()) {
                    (list.first() as VoDevice).isSelected = true
                    mList.value = list
                    frag?.let { (it as MainBodyCompostionPageFragment).deviceSelect(list.first() as VoDevice) }
                    getDataFromRoom(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID), (list.first() as VoDevice).model.DEVICE_ID)
                }
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }


        fun busArrivedInit(bus: String, deviceId: String, userId: String) {
            (frag as MainBodyCompostionPageFragment).setDeviceSelectByID(deviceId)
            busArrived(bus, deviceId, userId)
        }

        private fun busArrived(bus: String, deviceId: String, userId: String) {
            Log.i(deviceId)
            when (bus) {
                RxBusObj.BIO_BDY -> {
                    var v1: VoBioBdy? = null
                    var v2: VoBioBdy? = null
                    var v3: VoBioBdy? = null
                    var v4: VoBioBdy? = null
                    var v5: VoBioBdy? = null
                    var v6: VoBioBdy? = null
                    var v7: VoBioBdy? = null
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_A)?.let {
                        v1 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_B)?.let {
                        v2 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_C)?.let {
                        v3 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_D)?.let {
                        v4 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_E)?.let {
                        v5 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_F)?.let {
                        v6 = it
                    }
                    DBManager.withDB().withBioBdy().selectLast(deviceId, userId, PBean.TYPE_BDY_G)?.let {
                        v7 = it
                    }
//                    Log.i(v1!!.first, v2!!.first, v3!!.first, v4!!.first, v5!!.first, v6!!.first, v7!!.first)
                    (frag as MainBodyCompostionPageFragment).setBdy(v1, v2, v3, v4, v5, v6, v7)
                }
            }
        }

        fun getDataFromRoom(userId: String, deviceId: String) {
            busArrived(RxBusObj.BIO_BDY, deviceId, userId)
        }
    }
}