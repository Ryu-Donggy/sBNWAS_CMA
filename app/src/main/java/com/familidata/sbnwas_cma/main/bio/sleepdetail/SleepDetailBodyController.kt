package com.familidata.sbnwas_cma.main.bio.sleepdetail

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestSleepStageSyncData
import com.familidata.sbnwas_cma.api.res.BioSyncData
import com.familidata.sbnwas_cma.api.res.ResponseBioSyncData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.BioSleepStage
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SleepDetailBodyController(override var pCon: Context, var sessionID: Long) : IController() {

    var todayWork: WorkPlan? = null
    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = SleepDetailBodyFragment.with(this, vModel)
        return frag
    }


    override fun onClicking(view: View) {
        super.onClicking(view)
        if (view.id == R.id.ivBack || view.id == R.id.tvTitle) {
            frag?.requireActivity()?.finish()
        }
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {

            var count = DBManager.withDB().withBioSleepStage().selectStageCount(sessionID)
            mitem.value = DBManager.withDB().withBioSleepSession().selectOneForMonitor(sessionID)

            if (count > 0) {
                mList.value = DBManager.withDB().withBioSleepStage().selectForMonitor(sessionID)
            } else {
                getSleepStageSync(sessionID, {
                    mList.value = DBManager.withDB().withBioSleepStage().selectForMonitor(sessionID)
                })
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }

    }

    private fun getSleepStageSync(sessionId: Long, paramFunc: (Boolean) -> Unit) {
        ApiService.getSleepStageSync(
            RequestSleepStageSyncData(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                sessionId = sessionId
            )
        ).enqueue(object : Callback<ResponseBioSyncData> {
            override fun onResponse(call: Call<ResponseBioSyncData>, response: Response<ResponseBioSyncData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }

                            it.datas?.let { it2 ->
                                for (temp: BioSyncData in it2) {
                                    if (temp.data_type == "SLEEP_STAGE") {
                                        DBManager.withDB().withBioSleepStage().createSleepStageMessageDatas(temp)
                                    }
                                }
                            }
                            paramFunc(true)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call<ResponseBioSyncData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

}