package com.familidata.sbnwas_cma.main.settingmonitor

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.api.res.CmsConfig
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.api.res.ResponseUserInfoData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoMyPage
import com.familidata.sbnwas_cma.base.model.vo.VoSettingMonitor
import com.familidata.sbnwas_cma.base.model.vo.VoSingleTitle
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingMonitorBodyController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = SettingMonitorBodyFragment.with(this, vModel)
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
            CommonApi().getCMSConfig {
                var list = mutableListOf<PBean>()
                var type = ""
                var nextBack = R.drawable.ract_ffffff
                var i = 0
                for (temp: CmsConfig in it) {
//                    Log.i(temp.optName!!, temp.optType!!, temp.optPeriodType!!, temp.optValue)
                    if (type != temp.optType) {
                        type = temp.optType!!
                        list.add(VoSingleTitle(temp.optType))
                        nextBack = R.drawable.ract_rounded15_top_ffffff
                        if (i != 0) {
                            list[i - 1].backImage = R.drawable.ract_rounded15_bottom_ffffff
                            list[i - 1].bottomLineVisible = false
                        }
                        i++
                    }
                    list.add(VoSettingMonitor(temp, nextBack))
                    nextBack = R.drawable.ract_ffffff
                    i++
                }
                if(list.isNotEmpty()) {
                    list.last().backImage = R.drawable.ract_rounded15_bottom_ffffff
                    list.last().bottomLineVisible = false
                }
                mList.value = list
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }
    }
}