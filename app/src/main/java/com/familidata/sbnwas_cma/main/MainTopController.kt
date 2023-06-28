package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoUserInfo
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.devicemonitor.DeviceMonitorActivity
import com.familidata.sbnwas_cma.main.mypage.MyPageActivity
import com.familidata.sbnwas_cma.main.notification.NotificationMainActivity
import com.familidata.sbnwas_cma.main.settingmonitor.SettingMonitorActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj

class MainTopController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainTopFragment.with(this, vModel)
        return frag
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.iv_user_info -> {
                frag?.let { MyPageActivity.launch(it.requireActivity()) }
            }
            R.id.iv_notice -> {
                frag?.let { NotificationMainActivity.launch(it.requireActivity()) }
            }
        }
    }

    override fun spinnerSelectedStr(str: String) {
        super.spinnerSelectedStr(str)
//        val options = listOf("디바이스 목록", "설정 모니터")
        if (str != pCon.getString(R.string.device_list)) {
            frag?.let { SettingMonitorActivity.launch(it.requireActivity()) }
        } else {
            frag?.let { DeviceMonitorActivity.launch(it.requireActivity()) }
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {

            return mList
        }

        override fun getItem(): LiveData<PBean> {
            getDataFromRoom()
            return mitem
        }

        fun getDataFromRoom() {
            mitem.value = VoUserInfo(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_NAME))
        }
    }
}