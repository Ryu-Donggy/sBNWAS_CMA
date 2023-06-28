package com.familidata.sbnwas_cma

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.common.ServiceManager
import com.familidata.sbnwas_cma.databinding.ActivityInitBinding
import com.familidata.sbnwas_cma.databinding.ActivitySplashBinding
import com.familidata.sbnwas_cma.login.LoginActivity
import com.familidata.sbnwas_cma.main.MainActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.UnknownHostException


class SplashActivity : AppCompatActivity() {

    companion object {
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }


    private val binding by lazy { DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash) }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            if (!isInternetAvailable()) {
                MainActivity.launch(this@SplashActivity)
                finish()
            } else {
                toSomething();
            }
        }
    }


    fun toSomething() {
        runOnUiThread {
            CommonApi().getBioSync {
                CommonApi().getWorkPlan {
                    MainActivity.launch(this@SplashActivity)
                    finish()
                }
            }
        }
    }

    fun isInternetAvailable(): Boolean {
        try {
            val address: InetAddress = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }
}


