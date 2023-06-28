package com.familidata.sbnwas_cma.main

import android.content.Context
import android.view.View
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.analyze.AnalyzeActivity
import com.familidata.sbnwas_cma.main.analyze.MedicalCheckUpAnalyzeActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.rx.RxBusObj
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPagerController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        frag = MainPagerFragment.with(this)
        return frag
    }

    override fun setBusStation() {
        try {
            val station = CmaApplication.instance.bus()?.toObservable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                if (it is Pair<String, String>) {
                    busArrived(it.first, it.second)
                }
            }
            frag?.let {
                if (station != null) {
                    it.disposeBag.add(station)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun busArrived(bus: String, userId: String) {
        if (DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID) != userId) {
            return
        }
        when (bus) {
            RxBusObj.MAIN_PAGE_0, RxBusObj.MAIN_PAGE_1, RxBusObj.MAIN_PAGE_2, RxBusObj.MAIN_PAGE_3 -> {
                (frag as MainPagerFragment).setPage(bus)
            }
        }
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        if (view.id == R.id.cl_analyze) {
            frag?.let { MedicalCheckUpAnalyzeActivity.launch(it.requireActivity()) }
        } else if (view.id == R.id.cl_total_analyze) {
            frag?.let { AnalyzeActivity.launch(it.requireActivity()) }
        }
    }
}