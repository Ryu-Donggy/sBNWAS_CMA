package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

open class VoBarEntry(var entryList: List<BarEntry>, var isInt: Boolean) : PBean() {

}