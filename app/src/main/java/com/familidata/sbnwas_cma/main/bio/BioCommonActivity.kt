package com.familidata.sbnwas_cma.main.bio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityBioCommonMonitorBinding
import com.familidata.sbnwas_cma.main.MainDeviceListController

class BioCommonActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity, pBeanViewType: Int) {
            val intent = Intent(pAct, BioCommonActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("TYPE", pBeanViewType)
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private var title = ""

    private lateinit var mBioCommonTopController: BioCommonTopController
    private val mMainDeviceListController: MainDeviceListController by lazy { MainDeviceListController(this) }
    private lateinit var mchartController: BioCommonChartController
    private lateinit var bodyController: BioCommonBodyController
    private val binding by lazy { DataBindingUtil.setContentView<ActivityBioCommonMonitorBinding>(this, R.layout.activity_bio_common_monitor) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.controller = this
        bodyController = BioCommonBodyController(this, intent.getIntExtra("TYPE", 0))
        mchartController = BioCommonChartController(this, intent.getIntExtra("TYPE", 0))
        when (intent.getIntExtra("TYPE", 0)) {
            PBean.TYPE_BLP -> {
                title = getString(R.string.blp_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BLS -> {
                title = getString(R.string.bls_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_HTR -> {
                title = getString(R.string.htr_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_OXS -> {
                title = getString(R.string.oxs_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BAS_HSM -> {
                title = getString(R.string.hsm_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BAS_LSM -> {
                title = getString(R.string.lsm) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_A -> {
                title = getString(R.string.weight_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_B -> {
                title = getString(R.string.bfm_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_C -> {
                title = getString(R.string.bo_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_D -> {
                title = getString(R.string.bmi2) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_F -> {
                title = getString(R.string.body_water_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_BDY_G -> {
                title = getString(R.string.bfm_propotion_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_ECG -> {
                title = getString(R.string.ecg_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_SLEEP -> {
                title = getString(R.string.sleep_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_TMM -> {
                title = getString(R.string.tmm_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_CALORIE -> {
                title = getString(R.string.calorie_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_STEP -> {
                title = getString(R.string.walk_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_DISTANCE -> {
                title = getString(R.string.distance_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_ACTIVITY -> {
                title = getString(R.string.activity_title) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_CHO_TC -> {
                title = getString(R.string.cho_total) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_CHO_TG -> {
                title = getString(R.string.cho_tri) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_CHO_HDL -> {
                title = getString(R.string.cho_hdl) + " " + getString(R.string.monitor)
            }

            PBean.TYPE_CHO_LDL -> {
                title = getString(R.string.cho_ldl) + " " + getString(R.string.monitor)
            }
        }
        setTitle(title)
        mBioCommonTopController = BioCommonTopController(this, title)
        pFragReplace(binding.flTop.id, mBioCommonTopController.asFragCreate())
        pFragReplace(binding.flDevice.id, mMainDeviceListController.asFragCreate())
        pFragReplace(binding.flChart.id, mchartController.asFragCreate())
        pFragReplace(binding.flBody.id, bodyController.asFragCreate())


    }

    fun onClicking(view: View) {
        finish()
    }

}