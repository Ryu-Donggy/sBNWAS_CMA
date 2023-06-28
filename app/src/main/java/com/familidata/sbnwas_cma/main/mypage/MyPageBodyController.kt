package com.familidata.sbnwas_cma.main.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.api.res.ResponseUserInfoData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoMyPage
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageBodyController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MyPageBodyFragment.with(this, vModel)
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

            ApiService.getUserInfo(req).enqueue(object : Callback<ResponseUserInfoData> {
                override fun onResponse(call: Call<ResponseUserInfoData>, response: Response<ResponseUserInfoData>) {
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
                                    it2.loginId = Aria.withFirst().Decrypt(it2.loginId)
                                    it2.userName = Aria.withFirst().Decrypt(it2.userName)
                                    it2.gender = Aria.withFirst().Decrypt(it2.gender)
                                    it2.birth = Aria.withFirst().Decrypt(it2.birth)
                                    it2.telNo = Aria.withFirst().Decrypt(it2.telNo)
                                    it2.zipCode = Aria.withFirst().Decrypt(it2.zipCode)
                                    it2.address = Aria.withFirst().Decrypt(it2.address)
                                    it2.joinDate = Aria.withFirst().Decrypt(it2.joinDate)
                                    it2.empId = Aria.withFirst().Decrypt(it2.empId)
                                    it2.height = Aria.withFirst().Decrypt(it2.height)
                                    it2.weight = Aria.withFirst().Decrypt(it2.weight)
                                    it2.age = Aria.withFirst().Decrypt(it2.age)
                                    mitem.value = VoMyPage(it2)
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

                override fun onFailure(call: Call<ResponseUserInfoData>, t: Throwable) {
                    Log.d("${t.message}")
                    frag?.let {
                        CommonUtil.showToast(it.requireContext(), it.getString(R.string.time_out))

                    }
                }
            })
        }
    }


}