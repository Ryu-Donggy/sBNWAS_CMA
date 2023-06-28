package com.familidata.sbnwas_cma.main.notification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityWorkMainBinding
import com.familidata.sbnwas_cma.main.bio.BioCommonTopController

class NotificationMainActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, NotificationMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val mBioCommonTopController: BioCommonTopController by lazy { BioCommonTopController(this,getString(R.string.noti_monitor)) }
    private lateinit var mPage: NotificationPagerController
    private val binding by lazy { DataBindingUtil.setContentView<ActivityWorkMainBinding>(this, R.layout.activity_work_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = NotificationPagerController(this)
        pFragReplace(binding.flTop.id, mBioCommonTopController.asFragCreate())
        pFragReplace(binding.flBody.id, mPage.asFragCreate())
    }
}