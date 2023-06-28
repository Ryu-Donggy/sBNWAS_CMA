package com.familidata.sbnwas_cma.main.bio

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
import com.familidata.sbnwas_cma.main.settingmonitor.SettingMonitorActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj

class BioCommonTopController(override var pCon: Context, val title: String) : IController() {

    override fun asFragCreate(): PFragment? {
        frag = BioCommonTopFragment.with(this, title)
        return frag
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.ivBack -> {
                frag?.requireActivity()?.finish()
            }

        }
    }
}