package com.familidata.sbnwas_cma.base.model.vo

import com.familidata.sbnwas_cma.base.model.PBean
import com.github.mikephil.charting.data.Entry

open class VoEntry(var entryList: List<Entry>, var isInt: Boolean) : PBean() {
}