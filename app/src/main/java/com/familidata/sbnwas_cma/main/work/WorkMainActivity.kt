package com.familidata.sbnwas_cma.main.work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityWorkMainBinding
import com.familidata.sbnwas_cma.main.MainTopController

class WorkMainActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, WorkMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val mMainTopController: MainTopController by lazy { MainTopController(this) }
    private lateinit var mWorkPagerController: WorkPagerController
    private val binding by lazy { DataBindingUtil.setContentView<ActivityWorkMainBinding>(this, R.layout.activity_work_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWorkPagerController = WorkPagerController(this)
        pFragReplace(binding.flTop.id, mMainTopController.asFragCreate())
        pFragReplace(binding.flBody.id, mWorkPagerController.asFragCreate())
    }
}