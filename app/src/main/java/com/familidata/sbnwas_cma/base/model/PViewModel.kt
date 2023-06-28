package com.familidata.sbnwas_cma.base.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class PViewModel(pCon: Context) : ViewModel() {

    val mList: MutableLiveData<List<PBean>> = MutableLiveData()
    val mitem: MutableLiveData<PBean> = MutableLiveData()
    val mitem2: MutableLiveData<PBean> = MutableLiveData()


    abstract fun getList(): LiveData<List<PBean>>
    abstract fun getItem(): LiveData<PBean>

    open fun reflashMethod() {}
}
