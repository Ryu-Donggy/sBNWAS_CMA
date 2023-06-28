package com.familidata.sbnwas_cma.main.bio.sleepdetail

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.bio.BioCommonPieChartFragment
import com.familidata.sbnwas_cma.room.DBManager

class SleepDetailChartController(override var pCon: Context, var sessionID: Long) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = BioCommonPieChartFragment.with(this, vModel)
        return frag
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            val list = DBManager.withDB().withBioSleepSession().selectForMonitorChart(sessionID)
            mitem.value = VoPieEntry(list)

            return mitem
        }

    }

}