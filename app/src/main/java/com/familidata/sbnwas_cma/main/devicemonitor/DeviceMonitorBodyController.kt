package com.familidata.sbnwas_cma.main.devicemonitor

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.res.CmsConfig
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoSettingMonitor
import com.familidata.sbnwas_cma.base.model.vo.VoSingleTitle
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.room.DBManager

class DeviceMonitorBodyController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = DeviceMonitorBodyFragment.with(this, vModel)
        return frag
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.ivBack, R.id.tvTitle -> {
                frag?.requireActivity()?.finish()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            CommonApi().getDeviceList{
                mList.value = DBManager.withDB().withDeviceConfig().selectForMonitor()
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }
    }
}