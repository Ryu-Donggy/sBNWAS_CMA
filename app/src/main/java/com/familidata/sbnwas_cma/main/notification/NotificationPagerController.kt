package com.familidata.sbnwas_cma.main.notification

import android.content.Context
import android.view.View
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment

class NotificationPagerController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {

        frag = NotificationPageFragment.with(this)
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
                (frag as NotificationPageFragment).setPage(0)
            }
            else -> {
                (frag as NotificationPageFragment).setPage(1)
            }
        }
    }

}