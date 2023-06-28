package com.familidata.sbnwas_cma.main.bio

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoBioBas
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorData
import com.familidata.sbnwas_cma.base.model.vo.VoBioSleepHeader
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.bio.sleepdetail.SleepDetailActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.entity.BioBas
import com.familidata.sbnwas_cma.room.entity.BioSleepSession
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.rx.RxBusObj

class BioCommonBodyController(override var pCon: Context, var pBeanViewType: Int) : IController() {

    var todayWork: WorkPlan? = null
    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()

        var title = ""
        when (pBeanViewType) {
            PBean.TYPE_BLP -> {
                title = pCon.getString(R.string.blp_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BLS -> {
                title = pCon.getString(R.string.bls_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_HTR -> {
                title = pCon.getString(R.string.htr_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_OXS -> {
                title = pCon.getString(R.string.oxs_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BAS_HSM -> {
                title = pCon.getString(R.string.hsm_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BAS_LSM -> {
                title = pCon.getString(R.string.lsm) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_A -> {
                title = pCon.getString(R.string.weight_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_B -> {
                title = pCon.getString(R.string.bfm_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_C -> {
                title = pCon.getString(R.string.bo_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_D -> {
                title = pCon.getString(R.string.bmi2) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_F -> {
                title = pCon.getString(R.string.body_water_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_BDY_G -> {
                title = pCon.getString(R.string.bfm_propotion_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_ECG -> {
                title = pCon.getString(R.string.ecg_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_SLEEP -> {
                title = pCon.getString(R.string.sleep_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_TMM -> {
                title = pCon.getString(R.string.tmm_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_CALORIE -> {
                title = pCon.getString(R.string.calorie_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_STEP -> {
                title = pCon.getString(R.string.walk_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_DISTANCE -> {
                title = pCon.getString(R.string.distance_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_ACTIVITY -> {
                title = pCon.getString(R.string.activity_title) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_CHO_TG -> {
                title = pCon.getString(R.string.cho_tri) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_CHO_HDL -> {
                title = pCon.getString(R.string.cho_hdl) + " " + pCon.getString(R.string.monitor)
            }

            PBean.TYPE_CHO_LDL -> {
                title = pCon.getString(R.string.cho_ldl) + " " + pCon.getString(R.string.monitor)
            }
        }

        frag = BioCommonListFragment.with(this, vModel, title)
        return frag
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        if (view.id == R.id.ivBack || view.id == R.id.tvTitle) {
            frag?.requireActivity()?.finish()
        }
    }

    override fun onClicking(view: View, item: PBean) {
        super.onClicking(view, item)
        if (item is VoBioMonitorData) {
            Log.i("TODODODOODODOD", item.busType)
            if (item.busType == RxBusObj.BIO_WATCH_SLEEP) {
                if (item.entity is BioSleepSession) {
                    var sessionId = item.entity.SESSION_ID
                    frag?.requireActivity()?.let { SleepDetailActivity.launch(it, sessionId) }
                }
            }

            if (pBeanViewType == PBean.TYPE_BAS_HSM) {
                if (item.entity is BioBas) frag?.requireContext()?.let { Mp3PlayerDialog(it, item.entity).show() }
            } else if (pBeanViewType == PBean.TYPE_BAS_LSM) {
                if (item.entity is BioBas) frag?.requireContext()?.let { Mp3PlayerDialog(it, item.entity).show() }
            }


        } else if (item is VoBioSleepHeader) {
            frag?.requireActivity()?.let { SleepDetailActivity.launch(it, item.vo.SESSION_ID) }
        } else if (item is VoBioBas) {
            frag?.requireContext()?.let { Mp3PlayerDialog(it, item.vo).show() }
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            when (pBeanViewType) {
                PBean.TYPE_BLP -> {
                    val list = DBManager.withDB().withBioBlp().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioBlp().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_BLS -> {
                    val list = DBManager.withDB().withBioBls().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioBls().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_HTR -> {
                    val list = DBManager.withDB().withBioHtr().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioHtr().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_OXS -> {
                    val list = DBManager.withDB().withBioOxs().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioOxs().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_BAS_HSM -> {
                    val list = DBManager.withDB().withBioBas().selectForHsmMonitor()
                    mitem.value = DBManager.withDB().withBioBas().selectOneHsmForMonitor()
                    mList.value = list
                }

                PBean.TYPE_BAS_LSM -> {
                    val list = DBManager.withDB().withBioBas().selectForLsmMonitor()
                    mitem.value = DBManager.withDB().withBioBas().selectOneLsmForMonitor()
                    mList.value = list
                }

                PBean.TYPE_BDY_A, PBean.TYPE_BDY_B, PBean.TYPE_BDY_C, PBean.TYPE_BDY_D, PBean.TYPE_BDY_F, PBean.TYPE_BDY_G -> {
                    val list = DBManager.withDB().withBioBdy().selectForMonitor(pBeanViewType)
                    mitem.value = DBManager.withDB().withBioBdy().selectOneForMonitor(pBeanViewType)
                    mList.value = list
                }

                PBean.TYPE_ECG -> {
                    val list = DBManager.withDB().withBioEcgHeader().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioEcgHeader().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_SLEEP -> {
                    val list = DBManager.withDB().withBioSleepSession().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioSleepSession().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_TMM -> {
                    val list = DBManager.withDB().withBioTmm().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioTmm().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_CALORIE -> {
                    val list = DBManager.withDB().withBioCalorie().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioCalorie().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_STEP -> {
                    val list = DBManager.withDB().withBioStep().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioStep().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_DISTANCE -> {
                    val list = DBManager.withDB().withBioDistance().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioDistance().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_ACTIVITY -> {
                    val list = DBManager.withDB().withBioActivity().selectForMonitor()
                    mitem.value = DBManager.withDB().withBioActivity().selectOneForMonitor()
                    mList.value = list
                }

                PBean.TYPE_CHO_TC -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitor(RxBusObj.BIO_CHO_TC)
                    mitem.value = DBManager.withDB().withBioCho().selectOneForMonitor(RxBusObj.BIO_CHO_TC)
                    mList.value = list
                }

                PBean.TYPE_CHO_TG -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitor(RxBusObj.BIO_CHO_TG)
                    mitem.value = DBManager.withDB().withBioCho().selectOneForMonitor(RxBusObj.BIO_CHO_TG)
                    mList.value = list
                }

                PBean.TYPE_CHO_HDL -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitor(RxBusObj.BIO_CHO_HDL)
                    mitem.value = DBManager.withDB().withBioCho().selectOneForMonitor(RxBusObj.BIO_CHO_HDL)
                    mList.value = list
                }

                PBean.TYPE_CHO_LDL -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitor(RxBusObj.BIO_CHO_LDL)
                    mitem.value = DBManager.withDB().withBioCho().selectOneForMonitor(RxBusObj.BIO_CHO_LDL)
                    mList.value = list
                }
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }

    }

}