package com.familidata.sbnwas_cma.main.bio.sleepdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityBioSleepDetailBinding
import com.familidata.sbnwas_cma.main.MainDeviceListController
import com.familidata.sbnwas_cma.main.bio.BioCommonTopController

class SleepDetailActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity, sessionID: Long) {
            val intent = Intent(pAct, SleepDetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("SESSION_ID", sessionID)
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }


    private lateinit var mBioCommonTopController: BioCommonTopController
    private val mMainDeviceListController: MainDeviceListController by lazy { MainDeviceListController(this) }
    private lateinit var mchartController: SleepDetailChartController
    private lateinit var bodyController: SleepDetailBodyController
    private val binding by lazy { DataBindingUtil.setContentView<ActivityBioSleepDetailBinding>(this, R.layout.activity_bio_sleep_detail) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bodyController = SleepDetailBodyController(this, intent.getLongExtra("SESSION_ID", 0L))
        mchartController = SleepDetailChartController(this, intent.getLongExtra("SESSION_ID", 0L))
        mBioCommonTopController = BioCommonTopController(this, getString(R.string.sleep_detail))
        binding.controller = this
        pFragReplace(binding.flTop.id, mBioCommonTopController.asFragCreate())
        pFragReplace(binding.flDevice.id, mMainDeviceListController.asFragCreate())
        pFragReplace(binding.flChart.id, mchartController.asFragCreate())
        pFragReplace(binding.flBody.id, bodyController.asFragCreate())
    }

    fun onClicking(view: View) {
        finish()
    }
}

