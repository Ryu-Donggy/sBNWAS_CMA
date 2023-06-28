package com.familidata.sbnwas_cma.login

import android.content.Context
import android.view.View
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.SplashActivity
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.common.CommonApi
import com.familidata.sbnwas_cma.api.req.RequestLoginData
import com.familidata.sbnwas_cma.api.res.ResponseLoginData
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import com.familidata.sbnwas_cma.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginController(override var pCon: Context) : IController() {

    override fun asFragCreate(): PFragment? {
        frag = LoginFragment.with(this, DBManager.withDB().withProperty().getProperty(PropertyObj.LOGIN_ID))
        return frag
    }

    override fun onClicking(view: View) {
        super.onClicking(view)
        (frag as LoginFragment).checkLogin()?.let { login(it) }
    }

    private fun login(param: RequestLoginData) {
        Log.i(param)
        ApiService.getLogin(param).enqueue(object : Callback<ResponseLoginData> {
            override fun onResponse(call: Call<ResponseLoginData>, response: Response<ResponseLoginData>) {
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

//                                    USER_DISABLE_EXCEPTION(401, "User Disable Exception", "2003"),
//                                    USER_LOCKED_EXCEPTION(401, "User Locked Exception", "2004"),
//                                    USER_EXPIRED_EXCEPTION(401, "Account Expired Exception", "2005"),
//                                    CREDENTIAL_EXPIRED_EXCEPTION(401, "Credential Expired Exception", "2006"),
                                }
                                return
                            }

                            it.data?.let { it2 ->
                                DBManager.withDB().withProperty().setProperty(PropertyObj.LOGIN_ID, it2.loginId)
                                DBManager.withDB().withProperty().setProperty(PropertyObj.USER_NAME, it2.userName)
                                DBManager.withDB().withProperty().setProperty(PropertyObj.USER_ID, it2.userId)


                                DBManager.withDB().withProperty().setProperty(PropertyObj.AGE, if (it2.age != "") Aria.withFirst().Decrypt(it2.age) else "")
                                DBManager.withDB().withProperty().setProperty(PropertyObj.GENDER, if (it2.gender != "") Aria.withFirst().Decrypt(it2.gender) else "")
                                DBManager.withDB().withProperty().setProperty(PropertyObj.HEIGHT, if (it2.height != "") Aria.withFirst().Decrypt(it2.height) else "")
                                DBManager.withDB().withProperty().setProperty(PropertyObj.WEIGHT, if (it2.weight != "") Aria.withFirst().Decrypt(it2.weight) else "")


                                Log.i(
                                    DBManager.withDB().withProperty().getProperty(PropertyObj.LOGIN_ID),
                                    DBManager.withDB().withProperty().getProperty(PropertyObj.USER_NAME),
                                    DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                                )
                                CommonApi().getCMSConfig {
                                    frag?.let { it1 ->
                                        CommonApi().getWorkPlan {
                                            CommonApi().getDeviceList {
                                                SplashActivity.launch(it1.requireActivity())
                                                it1.requireActivity().finish()
                                            }
                                        }
                                    }
                                }
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

            override fun onFailure(call: Call<ResponseLoginData>, t: Throwable) {
                Log.d("${t.message}")
                frag?.let {
                    CommonUtil.showToast(it.requireContext(), it.getString(R.string.time_out))

                }
            }
        })
    }
}