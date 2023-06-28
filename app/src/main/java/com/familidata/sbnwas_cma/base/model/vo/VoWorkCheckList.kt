package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.room.entity.WorkCheckList

class VoWorkCheckList(val item: WorkCheckList?) : PBean() {

    var checkTypeText: String = ""
    var periodText: String = ""

    var ecgVisible = true
    var blpVisible = true
    var htrVisible = true
    var oxsVisible = true
    var basVisible = true

    var ecgCheckVisible = false
    var blpCheckVisible = false
    var htrCheckVisible = false
    var oxsCheckVisible = false
    var basCheckVisible = false

    var atFirst = false

    init {
        this.viewType = TYPE_A
        item?.let {
            checkTypeText = when (it.CHECK_TYPE) {
                "0" -> CmaApplication.context?.getString(R.string.before_work_check)!!
                "1" -> CmaApplication.context?.getString(R.string.on_work_check)!!
                else -> ""
            }
            periodText = it.START_TIME + " ~ " + it.END_TIME


            if (it.ECG_CHECK == "X") {
                ecgVisible = false
            } else ecgCheckVisible = it.ECG_CHECK == "Y"

            if (it.BLP_CHECK == "X") {
                blpVisible = false
            } else blpCheckVisible = it.BLP_CHECK == "Y"

            if (it.HTR_CHECK == "X") {
                htrVisible = false
            } else htrCheckVisible = it.HTR_CHECK == "Y"

            if (it.OXS_CHECK == "X") {
                oxsVisible = false
            } else oxsCheckVisible = it.OXS_CHECK == "Y"

            if (it.BAS_CHECK == "X") {
                basVisible = false
            } else basCheckVisible = it.BAS_CHECK == "Y"
        }

    }
}