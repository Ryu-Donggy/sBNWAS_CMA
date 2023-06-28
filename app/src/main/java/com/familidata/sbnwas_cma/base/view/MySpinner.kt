package com.familidata.sbnwas_cma.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

@SuppressLint("AppCompatCustomView")
class MySpinner(context: Context?, attrs: AttributeSet?) : Spinner(context, attrs) {
    var listener: OnItemSelectedListener? = null
    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (listener != null) listener!!.onItemSelected(null, null, position, 0)
    }

    fun setOnItemSelectedEvenIfUnchangedListener(
        listener: OnItemSelectedListener?
    ) {
        this.listener = listener
    }
}