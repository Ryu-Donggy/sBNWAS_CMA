package com.familidata.sbnwas_cma.base.view

import android.content.Context
import android.view.View
import android.widget.SeekBar
import com.familidata.base.Log
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel

abstract class IController {

    abstract var pCon: Context
    var frag: PFragment? = null
    var vModel: PViewModel? = null

    abstract fun asFragCreate(): PFragment?

    open fun setBusStation() {}

    open fun onClicking(view: View, item: PBean) {}

    open fun onClicking(view: View) {}

    open fun onPageSelected(view: View, position: Int) {}
    open fun onLongClicking(view: View, item: PBean): Boolean {

        return false;
    }

    /** for toggle (Switch View) only
     * 토글이 포함된 모델일경우 오버라이드 해서 사용할것
     */
    open fun onSwitchChanged(v: View, isChecked: Boolean, item: PBean) {
        Log.i("DUER", "CHECKED " + isChecked)
    }


    /** for Seekbar
     * It must be overied to listen Seekbar event
     */
    open fun onSeekBarValueChanged(
        seekBar: SeekBar,
        progresValue: Int,
        fromUser: Boolean,
        item: PBean
    ) {
        Log.i("DUER", progresValue);
    }

    open fun onCheckBoxChangedFun(v: View, isChecked: Boolean) {
        Log.i("DUER", isChecked);
    }

    open fun onTextChanged(v: View, s: CharSequence, start: Int, before: Int, count: Int) {
        Log.i("tag", "onTextChanged $s")
    }

    open fun spinnerSelectedStr(str: String) {
        Log.i("tag", "spinnerSelectedStr $str")
    }

    open fun getModel(): PViewModel? {
        return vModel
    }

}
