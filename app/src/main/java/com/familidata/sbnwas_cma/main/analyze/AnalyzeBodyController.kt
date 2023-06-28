package com.familidata.sbnwas_cma.main.analyze

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.api.res.ResponseBioAnalyzeData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoAnalyze
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeBodyController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = AnalyzeBodyFragment.with(this, vModel)
        return frag
    }

    override fun onClicking(view: View) {
        super.onClicking(view)
        when (view.id) {
            R.id.ivBack, R.id.tvTitle -> {
                frag?.requireActivity()?.finish()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            getData()
            return mitem
        }

        private fun getData() {
            val req = RequestUserID(DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
            Log.i(req)
            ApiService.getBioAnalyze(req).enqueue(object : Callback<ResponseBioAnalyzeData> {
                override fun onResponse(call: Call<ResponseBioAnalyzeData>, response: Response<ResponseBioAnalyzeData>) {
                    try {
                        Log.d(response)
                        Log.d("${response.code()},${response.message()},${response.body()}")
                        if (response.code() == 200) {
                            val rsp = response.body()
                            rsp?.let {
                                it.error?.let { it2 ->
                                    Log.i(it2.code, it2.message)
                                    frag?.let { f ->
                                        CommonUtil.showToast(f.requireContext(), it2.code + "::" + it2.message)
                                    }
                                    return
                                }
                                it.data?.let { it2 ->
                                    mitem.value = VoAnalyze(it2)
                                }
                            }
                        } else {
                            frag?.let {
                                CommonUtil.showToast(it.requireContext(), "" + response.code() + " " + it.getString(R.string.time_out))

                            }
                        }
                    } catch (e: Exception) {
                        Log.d(e.printStackTrace())
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBioAnalyzeData>, t: Throwable) {
                    Log.d("${t.message}")
                    frag?.let {
                        CommonUtil.showToast(it.requireContext(), it.getString(R.string.time_out))
                    }
                }
            })
        }
    }
}