package com.familidata.sbnwas_cma.main.bio

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.rx.RxBusObj

class BioCommonChartController(override var pCon: Context, var pBeanViewType: Int) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()

        when (pBeanViewType) {
            PBean.TYPE_BLP -> {
                frag = BioCommonCandleStickChartFragment.with(this, vModel)
            }

            PBean.TYPE_BLS -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_HTR -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_OXS -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BAS_HSM -> {
                frag = BioCommonSimpleTextFragment.with(this, vModel)
            }

            PBean.TYPE_BAS_LSM -> {
//                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_A -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_B -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_C -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_D -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_F -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_BDY_G -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_ECG -> {
//                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_SLEEP -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_TMM -> {
                frag = BioCommonLineChartFragment.with(this, vModel)
            }

            PBean.TYPE_CALORIE -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_STEP -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_DISTANCE -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_ACTIVITY -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_CHO_TC -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_CHO_TG -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_CHO_HDL -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }

            PBean.TYPE_CHO_LDL -> {
                frag = BioCommonBarChartFragment.with(this, vModel)
            }
        }
        return frag
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            when (pBeanViewType) {
                PBean.TYPE_BLP -> {
                    val list = DBManager.withDB().withBioBlp().selectForMonitorChart()
                    mitem.value = VoCandleStickEntry(list)
                }

                PBean.TYPE_BLS -> {
                    val list = DBManager.withDB().withBioBls().selectForMonitorChart()
                    mitem.value = VoEntry(list, true)
                }

                PBean.TYPE_HTR -> {
                    val list = DBManager.withDB().withBioHtr().selectForMonitorChart()
                    mitem.value = VoEntry(list, true)
                }

                PBean.TYPE_OXS -> {
                    val list = DBManager.withDB().withBioOxs().selectForMonitorChart()
                    mitem.value = VoEntry(list, true)
                }
//                PBean.TYPE_BAS_HSM -> {
//                    val list = DBManager.withDB().withBioBas().selectForHsmMonitor()
//                    mitem.value = DBManager.withDB().withBioBas().selectOneHsmForMonitor()
//                    mList.value = list
//                }
//                PBean.TYPE_BAS_LSM -> {
//                    val list = DBManager.withDB().withBioBas().selectForLsmMonitor()
//                    mitem.value = DBManager.withDB().withBioBas().selectOneLsmForMonitor()
//                    mList.value = list
//                }
                PBean.TYPE_BDY_A, PBean.TYPE_BDY_B, PBean.TYPE_BDY_C, PBean.TYPE_BDY_D, PBean.TYPE_BDY_F, PBean.TYPE_BDY_G -> {
                    val list = DBManager.withDB().withBioBdy().selectForMonitorChart(pBeanViewType)
                    mitem.value = VoEntry(list, false)
                }
//                PBean.TYPE_ECG -> {
//                    val list = DBManager.withDB().withBioEcgHeader().selectForMonitor()
//                    mitem.value = DBManager.withDB().withBioEcgHeader().selectOneForMonitor()
//                    mList.value = list
//                }
                PBean.TYPE_SLEEP -> {
                    val list = DBManager.withDB().withBioSleepSession().selectForMonitorChart()
                    mitem.value = VoBarEntry(list, false)
                }

                PBean.TYPE_TMM -> {
                    val list = DBManager.withDB().withBioTmm().selectForMonitorChart()
                    mitem.value = VoEntry(list, false)
                }

                PBean.TYPE_CALORIE -> {
                    val list = DBManager.withDB().withBioCalorie().selectForMonitorChart()
                    mitem.value = VoBarEntry(list, false)
                }

                PBean.TYPE_STEP -> {
                    val list = DBManager.withDB().withBioStep().selectForMonitorChart()
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_DISTANCE -> {
                    val list = DBManager.withDB().withBioDistance().selectForMonitorChart()
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_ACTIVITY -> {
                    val list = DBManager.withDB().withBioActivity().selectForMonitorChart()
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_CHO_TC -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitorChart(RxBusObj.BIO_CHO_TC)
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_CHO_TG -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitorChart(RxBusObj.BIO_CHO_TG)
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_CHO_HDL -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitorChart(RxBusObj.BIO_CHO_HDL)
                    mitem.value = VoBarEntry(list, true)
                }

                PBean.TYPE_CHO_LDL -> {
                    val list = DBManager.withDB().withBioCho().selectForMonitorChart(RxBusObj.BIO_CHO_LDL)
                    mitem.value = VoBarEntry(list, true)
                }
            }
            return mitem
        }
    }
}