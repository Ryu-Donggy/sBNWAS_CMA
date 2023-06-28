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
@Deprecated("NO LONGER USEFUL", level = DeprecationLevel.WARNING)
class MainBodyController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainBodyFragment.with(this, vModel)
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

    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.clWatchBlp -> {
                if (DBManager.withDB().withBioBlp().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_BLP) }
            }

            R.id.clWatchEcg -> {
                if (DBManager.withDB().withBioEcgHeader().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_ECG) }
            }

            R.id.clWatchHtr -> {
                if (DBManager.withDB().withBioHtr().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_HTR) }
            }

            R.id.clWatchOxs -> {
                if (DBManager.withDB().withBioOxs().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_OXS) }
            }

            R.id.clWatchSleep -> {
                if (DBManager.withDB().withBioSleepSession().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_SLEEP) }
            }

            R.id.clWatchCalorie -> {
                if (DBManager.withDB().withBioCalorie().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_CALORIE) }
            }

            R.id.clWatchStep -> {
                if (DBManager.withDB().withBioStep().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_STEP) }
            }

            R.id.clWatchDistance -> {
                if (DBManager.withDB().withBioDistance().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_DISTANCE) }
            }

            R.id.clWatchActivity -> {
                if (DBManager.withDB().withBioActivity().getCnt() > 0) frag?.let { BioCommonActivity.launch(it.requireActivity(), PBean.TYPE_ACTIVITY) }
            }

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

            R.id.cl_analyze -> {
                frag?.let { AnalyzeActivity.launch(it.requireActivity()) }
            }
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


        private fun watchData(bus: String, watchId: String, userId: String) {
            when (bus) {
                RxBusObj.BIO_WATCH_BLP -> {
                    DBManager.withDB().withBioBlp().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_HTR -> {
                    DBManager.withDB().withBioHtr().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_OXY -> {
                    DBManager.withDB().withBioOxs().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_ECG -> {
                    DBManager.withDB().withBioEcgHeader().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_SLEEP -> {
                    DBManager.withDB().withBioSleepSession().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_CALORIE -> {
                    DBManager.withDB().withBioCalorie().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_STEP -> {
                    DBManager.withDB().withBioStep().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_DISTANCE -> {
                    DBManager.withDB().withBioDistance().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }

                RxBusObj.BIO_WATCH_ACTIVITY -> {
                    DBManager.withDB().withBioActivity().selectLast(watchId, userId)?.let {
                        (frag as MainBodyFragment).setWatchBind(it)
                    } ?: run {
                        (frag as MainBodyFragment).hideWatchData(bus)
                    }
                }
            }
        }

        fun busArrived(bus: String, deviceId: String, userId: String) {

            when (bus) {
                RxBusObj.BIO_BLP -> {
                    DBManager.withDB().withBioBlp().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_ECG -> {
                    DBManager.withDB().withBioEcg().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_GPS -> {
//                   mitem.value = DBManager.withDB().withBioGps().selectLast(deviceId, userId)
                }

                RxBusObj.BIO_GYR -> {
//                   mitem.value = DBManager.withDB().withBioGyr().selectLast(deviceId, userId)
                }

                RxBusObj.BIO_HTR -> {
                    DBManager.withDB().withBioHtr().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_OXY -> {
                    DBManager.withDB().withBioOxs().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_ACC -> {
//                   mitem.value = DBManager.withDB().withBioAcc().selectLast(deviceId, userId)
                }

                RxBusObj.BIO_BLS -> {
                    DBManager.withDB().withBioBls().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_BAS -> {
                    DBManager.withDB().withBioBas().selectLast(deviceId, userId, "H")?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                    DBManager.withDB().withBioBas().selectLast(deviceId, userId, "L")?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_BDY -> {
                    var v1: VoBioBdy? = null
                    var v2: VoBioBdy? = null
                    var v3: VoBioBdy? = null
                    var v4: VoBioBdy? = null
                    var v5: VoBioBdy? = null
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
                    if (v1 != null) (frag as MainBodyFragment).setBdy(v1, v2, v3, v4, v5)
                }

                RxBusObj.BIO_TMM -> {
                    DBManager.withDB().withBioTmm().selectLast(deviceId, userId)?.let {
                        (frag as MainBodyFragment).setItem(it)
                    }
                }

                RxBusObj.BIO_WATCH_BLP, RxBusObj.BIO_WATCH_OXY, RxBusObj.BIO_WATCH_HTR, RxBusObj.BIO_WATCH_ECG, RxBusObj.BIO_WATCH_SLEEP,
                RxBusObj.BIO_WATCH_CALORIE, RxBusObj.BIO_WATCH_STEP, RxBusObj.BIO_WATCH_DISTANCE, RxBusObj.BIO_WATCH_ACTIVITY,
                -> {
                    watchData(bus, deviceId, userId)
                }

            }
        }

        private fun getDataFromRoom(userId: String) {
            val watchId = DBManager.withDB().withDeviceConfig().selectWatchID()
            if (watchId == null) {
                (frag as MainBodyFragment).hideWatch()
            } else {
                Log.i("watchId", watchId)
                watchData(RxBusObj.BIO_WATCH_BLP, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_HTR, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_OXY, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_ECG, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_SLEEP, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_ACTIVITY, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_STEP, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_DISTANCE, watchId, userId)
                watchData(RxBusObj.BIO_WATCH_CALORIE, watchId, userId)
            }
            val deviceList = DBManager.withDB().withDeviceConfig().selectWithoutWatchNApp()
            for (temp: PBean in deviceList) {
                if (temp is VoDevice) {
                    busArrived(temp.model.DEVICE_TYPE, temp.model.DEVICE_ID, userId)

                }
            }
        }

    }
}