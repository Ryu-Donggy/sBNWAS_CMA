package com.familidata.sbnwas_cma.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.databinding.ActivityNLoginBinding

class LoginActivity : BaseActivity() {
    companion object {
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val mLoginController: LoginController by lazy { LoginController(this) }
    private val binding by lazy { DataBindingUtil.setContentView<ActivityNLoginBinding>(this, R.layout.activity_n_login) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DBManager.withDB().withWorkPlan().delete()
        pFragReplace(binding.flLogin.id, mLoginController.asFragCreate())
    }
}