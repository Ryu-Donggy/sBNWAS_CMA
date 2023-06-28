package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.WorkPlan

class VoWork(val model: WorkPlan?) : PBean() {


    var planTimeForView: String = ""
    var actualTimeForView: String = ""

    var btnStartVisible: Boolean = false
    var btnWrightLogVisible: Boolean = false
    var btnWorkDoenVisible: Boolean = false

    var topVisibleTxt = ""

    init {
        this.viewType = TYPE_A
        model?.let {
            this.planTimeForView = it.startTime + " ~ " + it.endTime
            this.actualTimeForView = it.workStartTime + " ~ " + it.workEndTime

            when (it.workStatus) {
                "0" -> {
                    this.model.workStatusText = CmaApplication.context?.getString(R.string.work_planed)!!
                    this.btnStartVisible = true
                }

                "1" -> {
                    this.model.workStatusText = CmaApplication.context?.getString(R.string.work_on)!!
                    this.btnWrightLogVisible = true
                }

                "2" -> {
                    this.model.workStatusText = CmaApplication.context?.getString(R.string.work_on)!!
                    this.btnWorkDoenVisible = true
                }
            }

            topVisibleTxt = CmaApplication.context?.getString(R.string.work_planed_daytime)!! + it.workDate + " " + planTimeForView +
                    "\n" + CmaApplication.context?.getString(R.string.work_start_time)!! + it.workStartTime +
                    "\n" + CmaApplication.context?.getString(R.string.work_place)!! + it.workPlace + "\n" + CmaApplication.context?.getString(R.string.work_role)!! + it.workRole
        }

//        t.setText(Html.fromHtml("7<sup>2</sup>"))
//        <import type="android.text.Html"/>
//        android:text="@{Html.fromHtml(String.format(@string/DateCreate,others.created))}" />
//        https://velog.io/@tura/constraint-layout-2-part-2-flow
    }
}