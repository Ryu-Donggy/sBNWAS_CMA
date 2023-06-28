package com.familidata.sbnwas_cma.main.notification

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.sbnwas_cma.CmaApplication
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestCommentData
import com.familidata.sbnwas_cma.api.req.RequestStatusData
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

class NotificationCMSController(override var pCon: Context) : IController() {

    var todayWork: WorkPlan? = null
    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = NotificationListFragment.with(this, vModel, pCon.getString(R.string.msg_current_30_days))

        return frag
    }

    override fun onClicking(view: View) {
        super.onClicking(view)

    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            mList.value = DBManager.withDB().withCmaNotice().selectForList("CMS")
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }

    }
}