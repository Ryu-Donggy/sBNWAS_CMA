package com.familidata.sbnwas_cma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.databinding.ActivityInitBinding
import com.familidata.sbnwas_cma.login.LoginActivity
import com.familidata.sbnwas_cma.main.MainActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj


class InitActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityInitBinding>(this, R.layout.activity_init) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID) != "") {
            SplashActivity.launch(this)
        } else {
            LoginActivity.launch(this)
        }
        finish()
    }
}


