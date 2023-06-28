package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestCommentData
import com.familidata.sbnwas_cma.api.req.RequestStatusData
import com.familidata.sbnwas_cma.api.res.ResWorkPlan
import com.familidata.sbnwas_cma.api.res.ResponseWorkStatusData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoWork
import com.familidata.sbnwas_cma.base.model.vo.VoWorkCheckList
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.work.WorkMainActivity
import com.familidata.sbnwas_cma.main.workstatus.WatchConnector
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.room.entity.WorkPlan
import com.familidata.sbnwas_cma.rx.RxBusObj
import com.familidata.sbnwas_cma.util.CommonUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class MainWorkController(override var pCon: Context, var progressVisible: (visible: Int) -> Unit) : IController() {
    val executor = Executors.newFixedThreadPool(1)
    var todayWork: WorkPlan? = null
    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainWorkFragment.with(this, vModel)

        return frag
    }

    override fun onClicking(view: View) {
        super.onClicking(view)


        if (view.id == R.id.bt_work_start) {
            frag?.requireContext()?.let {
                AlertDialog.Builder(it).setTitle("").setMessage(pCon.getString(R.string.start_work)).setPositiveButton(pCon.getString(R.string.confrim)) { _, _ ->
                    setBusStation()
                    progressVisible(View.VISIBLE)
                    (vModel as InViewModel).setWorkStatus()
                }.setNegativeButton(pCon.getString(R.string.cancel)) { _, _ -> }.create().show()
            }

        } else if (view.id == R.id.bt_work_diary) {
            frag?.requireContext()?.let { MainProfileDialog(it, this::dialogCallback).show() }
        } else if (view.id == R.id.bt_work_end) {
            frag?.requireContext()?.let {
                AlertDialog.Builder(it).setTitle("").setMessage(pCon.getString(R.string.end_work)).setPositiveButton(pCon.getString(R.string.confrim)) { _, _ ->
                    (vModel as InViewModel).setWorkStatus()
                }.setNegativeButton(pCon.getString(R.string.cancel)) { _, _ -> }.create().show()
            }

        } else if (view.id == R.id.ivMore) {
            frag?.let { WorkMainActivity.launch(it.requireActivity()) }
        }
    }


    override fun setBusStation() {
        try {
            val station = CmaApplication.instance.bus()?.toObservable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                if (it is Pair) {
                    if (it.first == RxBusObj.WORK_START_RES) progressVisible(View.GONE)
                    else if (it.first != RxBusObj.BIO_ACC && it.first != RxBusObj.BIO_GPS && it.first != RxBusObj.BIO_GYR) {
                        (vModel as InViewModel).getDataFromRoom()
                    }
                }
            }
            frag?.let {
                if (station != null) {
                    it.disposeBag.add(station)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun dialogCallback(txt: String) {
        Log.i(txt)
        (vModel as InViewModel).sendDialry(txt)
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {

            return mList
        }

        override fun getItem(): LiveData<PBean> {
            getDataFromRoom()
            return mitem
        }

        fun getDataFromRoom() {
            todayWork = DBManager.withDB().withWorkPlan().getUnFinishedWork()
            if (todayWork == null) {
                todayWork = DBManager.withDB().withWorkPlan().getTodayPlan()
            }
            mitem.value = VoWork(todayWork)
            todayWork?.let {
                val checkList = DBManager.withDB().withWorkCheckList().selectByWorkId(it.workId)
                for (temp: PBean in checkList) {
                    if (temp is VoWorkCheckList) {
                        if (temp.item!!.CHECK_TYPE == "0") {
                            mitem2.value = temp
                            break
                        }
                    }
                }
                if (checkList.isNotEmpty())
                    checkList.removeAt(0)
                mList.value = checkList
            }
        }

        fun setCheckListToRoom() {
            var nWork = DBManager.withDB().withWorkPlan().getUnFinishedWork()
            if (nWork == null) {
                nWork = DBManager.withDB().withWorkPlan().getTodayPlan()
            }
            nWork?.let {

                DBManager.withDB().withWorkCheckList().insert(
                    ResWorkPlan(
                        workId = it.workId, workDate = it.workDate, workPlace = it.workPlace, workRole = it.workRole, startTime = it.startTime, endTime = it.endTime, workStatus = it.workStatus, workStatusText = it.workStatusText, aps = ArrayList()
                    )
                )
            }
        }

        fun setWorkStatus() {
            if (todayWork == null) return
            var req = RequestStatusData(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                workId = todayWork?.workId,
                flag = if (todayWork?.workStatus == "0") {
                    "S"
                } else {
                    "F"
                },
                atime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),

                )
            ApiService.postWorkStatusExecution(req).enqueue(object : Callback<ResponseWorkStatusData> {
                override fun onResponse(call: Call<ResponseWorkStatusData>, response: Response<ResponseWorkStatusData>) {
                    try {
                        Log.d("${response.code()},${response.message()},${response.body()}")
                        if (response.code() == 200) {
                            val rsp = response.body()

                            rsp?.let {
                                it.error?.let { it2 ->
                                    Log.i(it2.code, it2.message)
                                    frag?.let { f ->
                                        CommonUtil.showToast(f.requireContext(), it2.code + "::" + it2.message)
                                        DBManager.withDB().passwordEncorrect()
                                        frag?.requireActivity()?.finishAffinity()
                                    }
                                    return
                                }
                                it.data?.let { it2 ->
                                    if (todayWork != null) {
                                        todayWork?.workStatus = it2.workStatus
                                        todayWork?.workStatusText = it2.workStatusText

                                        if (todayWork?.workStatus == "1") {
                                            todayWork?.workStartTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                                            val json = JSONObject()
                                            json.put(PropertyObj.InWork.WI_BIO_GYR, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GYR))
                                            json.put(PropertyObj.InWork.WI_BIO_ACC, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_ACC))
                                            json.put(PropertyObj.InWork.WI_BIO_GPS, DBManager.withDB().withProperty().getProperty(PropertyObj.InWork.WI_BIO_GPS))
                                            executor.execute(WatchConnector(context = pCon, json, executor).checkConnection())
                                        } else {
                                            val json = JSONObject()
                                            json.put("JOBS_DONE", "JOBS_DONE")
                                            executor.execute(WatchConnector(context = pCon, json, executor).checkConnection())
                                            todayWork?.workEndTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                                        }
                                        DBManager.withDB().withWorkPlan().update(todayWork)
//                                        mitem.value = VoWork(todayWork)

                                        if (todayWork?.workStatus == "3") {
                                            setCheckListToRoom()
                                        }

                                        getDataFromRoom()
                                    }
                                }
                            }
                        } else {
                            frag?.let {
                                CommonUtil.showToast(it.requireContext(), "" + response.code() + " " + it.getString(R.string.time_out))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("${e.printStackTrace()}")
                    }
                }

                override fun onFailure(call: Call<ResponseWorkStatusData>, t: Throwable) {
                    Log.d("${t.message}")
                    frag?.let {
                        CommonUtil.showToast(it.requireContext(), it.getString(R.string.time_out))
                    }
                }
            })
        }


        fun sendDialry(txt: String) {
            if (todayWork == null) return
            val req = RequestCommentData(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID), workId = todayWork?.workId, comments = txt
            )
            ApiService.postWorkDiary(req).enqueue(object : Callback<ResponseWorkStatusData> {
                override fun onResponse(call: Call<ResponseWorkStatusData>, response: Response<ResponseWorkStatusData>) {
                    try {
                        Log.d("${response.code()},${response.message()},${response.body()}")
                        if (response.code() == 200) {
                            val rsp = response.body()
                            rsp?.let {
                                it.error?.let { it2 ->
                                    Log.i(it2.code, it2.message)
                                    frag?.let { f ->
                                        CommonUtil.showToast(f.requireContext(), it2.code + "::" + it2.message)
                                        DBManager.withDB().passwordEncorrect()
                                        frag?.requireActivity()?.finishAffinity()
                                    }
                                    return
                                }
                                it.data?.let { it2 ->
                                    if (todayWork != null) {
                                        todayWork?.workStatus = it2.workStatus
                                        todayWork?.workStatusText = it2.workStatusText
                                        DBManager.withDB().withWorkPlan().update(todayWork)
                                        mitem.value = VoWork(todayWork)
                                    }
                                }
                            }
                        } else {
                            frag?.let {
                                CommonUtil.showToast(it.requireContext(), "" + response.code() + " " + it.getString(R.string.time_out))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("${e.printStackTrace()}")
                    }
                }

                override fun onFailure(call: Call<ResponseWorkStatusData>, t: Throwable) {
                    Log.d("${t.message}")
                    frag?.let {
                        CommonUtil.showToast(it.requireContext(), it.getString(R.string.time_out))
                    }
                }
            })
        }
    }
}