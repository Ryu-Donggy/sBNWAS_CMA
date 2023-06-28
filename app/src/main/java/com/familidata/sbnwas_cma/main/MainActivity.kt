package com.familidata.sbnwas_cma.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.*
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.familidata.base.Log
import com.familidata.sbnwas_cma.BuildConfig
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.BaseActivity
import com.familidata.sbnwas_cma.common.MessageService
import com.familidata.sbnwas_cma.common.ServiceManager
import com.familidata.sbnwas_cma.common.job.DataScheduler
import com.familidata.sbnwas_cma.databinding.ActivityNMainBinding
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    companion object {
        lateinit var prefs: ServiceManager
        fun launch(pAct: Activity) {
            val intent = Intent(pAct, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            pAct.startActivityForResult(intent, START_REQUEST_CODE)
            pAct.startActivity(intent)
            pAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val TAG = "MainActivity"
    private val mMainTopController: MainTopController by lazy { MainTopController(this) }
    private val mMainDeviceListController: MainDeviceListController by lazy { MainDeviceListController(this) }

    private val mMainPagerController: MainPagerController by lazy { MainPagerController(this) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityNMainBinding>(this, R.layout.activity_n_main) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = ServiceManager(applicationContext)
//        val status = prefs.getBoolean("running", false)
//        Log.d("=======================>", status.toString())
        pFragReplace(binding.flTop.id, mMainTopController.asFragCreate())
        pFragReplace(binding.flDevice.id, mMainDeviceListController.asFragCreate())
        pFragReplace(binding.flBody.id, mMainPagerController.asFragCreate())
        startService()
        initHC()
//        Log.i(getMacAddress())
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(intent.toString())
        intent?.let {
            MainBioLogAcceptor(this).setNewIntent(it)
        }
    }

    fun startService() {
        val msgIntent = Intent(applicationContext, MessageService::class.java)
        msgIntent.putExtra("id", DBManager.withDB().withProperty().getProperty(PropertyObj.LOGIN_ID))
        startForegroundService(msgIntent)
    }


    //헬스커넥트 권한 요청
    private val PERMISSIONS = setOf(
        Permission.createReadPermission(BloodPressure::class),
        Permission.createReadPermission(SleepSession::class),
        Permission.createReadPermission(SleepStage::class),
        Permission.createReadPermission(Distance::class),
        Permission.createReadPermission(Steps::class),
        Permission.createReadPermission(StepsCadenceSeries::class),
        Permission.createReadPermission(SpeedSeries::class),
        Permission.createReadPermission(ActivitySession::class),
        Permission.createReadPermission(TotalCaloriesBurned::class),
        Permission.createReadPermission(ActiveCaloriesBurned::class),
        Permission.createReadPermission(Vo2Max::class),
    )
    private val requestPermissions = registerForActivityResult(HealthDataRequestPermissions()) { granted ->
        if (granted.containsAll(PERMISSIONS)) {
            Log.i(TAG, "permissions granted")
        } else {
            Log.i(TAG, "permissions not granted")
            Toast.makeText(this, getString(R.string.msg_accept_permission_on_health_connect), Toast.LENGTH_SHORT).show()
//            finish()
        }
    }

    //헬스커넥트 설치 여부 확인 및 초기화
    private fun initHC() {
        if (HealthConnectClient.isAvailable(this)) {
            Log.i(TAG, "installed")
            val healthConnectClient = HealthConnectClient.getOrCreate(this)
            checkPermissions(healthConnectClient)
        } else {
            Log.i(TAG, "not installed")
        }
    }

    //헬스커넥트 권한 확인
    private fun checkPermissions(client: HealthConnectClient) {
        lifecycleScope.launch {
            val granted = client.permissionController.getGrantedPermissions(PERMISSIONS)
            if (granted.containsAll(PERMISSIONS)) {
                Log.i(TAG, "already granted")
                initWorkManager()
            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }
    }

    //스케줄러 초기화 설정
    private fun initWorkManager() {
//        WorkManager.getInstance(this).cancelAllWork()
        val workRequest = PeriodicWorkRequestBuilder<DataScheduler>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            BuildConfig.APPLICATION_ID, ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}