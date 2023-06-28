package com.familidata.sbnwas_cma.main.work

import android.content.Context
import android.view.View
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment

class WorkPagerController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {

        frag = WorkPageFragment.with(this)
        return frag
    }

    //
    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.ivBack, R.id.tvTitle -> {
                frag?.requireActivity()?.finish()
            }
            R.id.tvPlanSelected, R.id.tvPlanNonSelected -> {
                (frag as WorkPageFragment).setPage(0)
            }
            else -> {
                (frag as WorkPageFragment).setPage(1)
            }
        }
    }
//
//
//    override fun onClicking(view: View, item: PBean) {
//        super.onClicking(view, item)
//    }

}