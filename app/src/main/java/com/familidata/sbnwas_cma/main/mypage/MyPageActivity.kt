package com.familidata.sbnwas_cma.main.mypage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityCommonBinding
import com.familidata.sbnwas_cma.main.bio.BioCommonTopController

class MyPageActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, MyPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val mBioCommonTopController: BioCommonTopController by lazy { BioCommonTopController(this, getString(R.string.my_page)) }
    private val bodyController: MyPageBodyController by lazy { MyPageBodyController(this) }
    private val binding by lazy { DataBindingUtil.setContentView<ActivityCommonBinding>(this, R.layout.activity_common) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pFragReplace(binding.flTop.id, mBioCommonTopController.asFragCreate())
        pFragReplace(binding.flBody.id, bodyController.asFragCreate())
    }

}
