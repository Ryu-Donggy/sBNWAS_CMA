package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.BuildConfig
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.api.res.ResMember
import com.familidata.sbnwas_cma.api.res.ResponseMemberData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoDevice
import com.familidata.sbnwas_cma.base.model.vo.VoUsers
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.main.bio.BioCommonActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainDeviceListController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        vModel = InViewModel()
        frag = MainDeviceListFragment.with(this, vModel)
        return frag
    }

    var selectedVo: VoDevice? = null
    fun onSomething() {
        selectedVo?.let { item ->
            if (item.model.DEVICE_TYPE == "WTH" || item.model.DEVICE_TYPE == "APP") {

                var isIntent = item.model.APP_PACKAGE?.let { frag?.requireContext()?.getPackageManager()?.getLaunchIntentForPackage(it) }
                frag?.requireActivity()?.startActivity(isIntent)
                if (frag?.requireActivity() is BioCommonActivity) {
                    frag?.requireActivity()?.finish()
                }
                return
            }


            var compName = item.model.APP_PACKAGE?.let { item.model.APP_ACTIVITY?.let { it1 -> ComponentName(it, it1) } }
            var intent = Intent(Intent.ACTION_VIEW)
            try {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.putExtra("user_id", DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))

                // skeeper 임시 코드
                if ("9fa242e9-b9cc-4674-ac22-2b72a91A2660" != item.model.DEVICE_ID) {
                    intent.putExtra("user_name", DBManager.withDB().withProperty().getProperty(PropertyObj.USER_NAME))
                    intent.putExtra("user_email", DBManager.withDB().withProperty().getProperty(PropertyObj.LOGIN_ID))
                    intent.putExtra("age",DBManager.withDB().withProperty().getProperty(PropertyObj.AGE))
                    intent.putExtra("gender",DBManager.withDB().withProperty().getProperty(PropertyObj.GENDER))
                    intent.putExtra("weight",DBManager.withDB().withProperty().getProperty(PropertyObj.WEIGHT))
                    intent.putExtra("height",DBManager.withDB().withProperty().getProperty(PropertyObj.HEIGHT))

                    intent.putExtra("device_company", item.model.DEVICE_COMPANY)
                    intent.putExtra("device_name", item.model.DEVICE_NAME)
                    intent.putExtra("device_id", item.model.DEVICE_ID)
                    intent.putExtra("device_model", item.model.DEVICE_MODEL)
                    intent.putExtra("device_type", item.model.DEVICE_TYPE)
                    intent.putExtra("device_type_name", item.model.DEVICE_TYPE_NAME)
                    intent.putExtra("device_version", item.model.DEVICE_VERSION)

                    intent.putExtra("target_app", BuildConfig.APPLICATION_ID)
                    intent.putExtra("target_activity",("." + MainActivity::class.java).replaceFirst(".class ", ""))

                    val jsonArray = JSONArray()
                    for (temp: VoUsers in userList) {
                        jsonArray.put(temp.voToJson())
                    }
                    Log.i("users", jsonArray.toString())
                    intent.putExtra("users", jsonArray.toString())
                }
                intent.component = compName
                frag?.requireContext()?.startActivity(intent)
                if (frag?.requireActivity() is BioCommonActivity) {
                    frag?.requireActivity()?.finish()
                }

            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onClicking(view: View, item: PBean) {
        super.onClicking(view, item)
        if (item is VoDevice) {
            selectedVo = item
            getMembers { onSomething() }
        }
    }

    internal var userList: MutableList<VoUsers> = ArrayList()
    fun getMembers(paramFunc: (Boolean) -> Unit) {
        ApiService.getMembers(
            RequestUserID(userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID))
        ).enqueue(object : Callback<ResponseMemberData> {
            override fun onResponse(call: Call<ResponseMemberData>, response: Response<ResponseMemberData>) {
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
                                Log.i("members : " + it2)
                                userList = ArrayList()
                                for (temp: ResMember in it2) {
//                                    mListAdapter.insertItem()
                                    userList.add(
                                        VoUsers(
                                            userid = temp.userId,
                                            userName = temp.userName,
                                            userEmail = temp.loginId,
                                            age = if (temp.age != "") Aria.withFirst().Decrypt(temp.age) else "",
                                            gender = if (temp.gender != "") Aria.withFirst().Decrypt(temp.gender) else "",
                                            height = if (temp.height != "") Aria.withFirst().Decrypt(temp.height) else "",
                                            weight = if (temp.weight != "") Aria.withFirst().Decrypt(temp.weight) else "",
                                            telNo =  if (temp.telNo != "") Aria.withFirst().Decrypt(temp.telNo) else "",
                                        )
                                    )
                                }
                                paramFunc(true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call<ResponseMemberData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    open inner class InViewModel : PViewModel(pCon) {
        override fun getList(): LiveData<List<PBean>> {
            viewModelScope.launch {
                mList.value = DBManager.withDB().withDeviceConfig().select()
            }
            return mList
        }

        override fun getItem(): LiveData<PBean> {
            return mitem
        }


    }
}