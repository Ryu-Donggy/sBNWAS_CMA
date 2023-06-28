package com.familidata.sbnwas_cma.api

import com.familidata.sbnwas_cma.api.req.*
import com.familidata.sbnwas_cma.api.res.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.Executors


// H.Point 서버와 API 통신
object ApiService {

    //    private const val URL = "http://192.168.0.245:8080/"
    private const val URL_AT_FIRST = "http://220.74.21.20:14042/"
    const val URL = "http://sbnwas.com"

//         const val URL = "http://192.168.10.42:8080/"
    private var api: ApiCall? = null
    private var apiAtFirst: ApiCall? = null

    private fun getApi(): ApiCall {
        if (api == null) {
//            val headerInterceptor = Interceptor {
//                val reqBuilder = it.request()
//                    .newBuilder()
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("teOpsyGbcd", "01") // 01 : Android, 02 : iOS
//                    .addHeader("appVer", BuildConfig.VERSION_NAME)
//                    .addHeader("awakenYn", if (ActivityMain.isBackground) "Y" else "N")
//                    .addHeader("autoLoginYn", if (Preference.getAutoLogin()) "Y" else "N")
//
//                if (UserInfo.authKey.isNotEmpty())
//                    reqBuilder.addHeader("authKey", UserInfo.authKey)
//                if (UserInfo.mobileCardNo.isNotEmpty())
//                    reqBuilder.addHeader("mblCrdNo", UserInfo.mobileCardNo)
//                if (UserInfo.teInntId.isNotEmpty())
//                    reqBuilder.addHeader("connId", UserInfo.teInntId)
//
////                log.e("awakenYn [${ActivityMain.isBackground}], AuthKey [${UserInfo.authKey}]")
//
////                log.d("site[${Common.getBaseUrl()}], appVer[${BuildConfig.VERSION_NAME}], autoLogin[${if(Preference.getAutoLogin()) "Y" else "N"}]" +
////                        ", authKey[${UserInfo.authKey}], mobileCardNum[${UserInfo.mobileCardNo}], teInntId[${UserInfo.teInntId}]")
//
//                val req = reqBuilder.build()
//                return@Interceptor it.proceed(req)
//            }

//            val loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//sds5767!
//            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(headerInterceptor)
//                .addInterceptor(loggingInterceptor)
//                .addNetworkInterceptor(StethoInterceptor()) /*네트워크 디버깅용 스테토*/
//                .build()


            api = Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
                .build().create(ApiCall::class.java)
        }
        return api!!
    }

    fun getAPIAtBackground(): ApiCall {
        if (apiAtFirst == null) {
            apiAtFirst = Retrofit.Builder().baseUrl(URL_AT_FIRST).addConverterFactory(GsonConverterFactory.create()).callbackExecutor(Executors.newSingleThreadExecutor()).build().create(ApiCall::class.java)
        }
        return apiAtFirst!!
    }

    fun getAPIAtFirst(): ApiCall {
        if (apiAtFirst == null) {
            apiAtFirst = Retrofit.Builder().baseUrl(URL_AT_FIRST).addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
                .build().create(ApiCall::class.java)
        }
        return apiAtFirst!!
    }


    fun atFirst(req: RequestLoginData): Call<ResponseLoginData> {
        return getAPIAtFirst().getLogin(req)
    }

    fun getLogin(req: RequestLoginData): Call<ResponseLoginData> {
        return getApi().getLogin(req)
    }

    fun getWorkPlan(req: UserIdRequestData): Call<ResponseWorkPlanData> {
        return getApi().getWorkPlan(req)
    }

    fun postWorkStatusExecution(req: RequestStatusData): Call<ResponseWorkStatusData> {
        return getApi().postWorkStatusExecution(req)
    }

    fun postWorkDiary(req: RequestCommentData): Call<ResponseWorkStatusData> {
        return getApi().postWorkDiary(req)
    }

    fun getCMSConfig(@Body req: RequestUserID): Call<ResponseCmsConfigData> {
        return getApi().getCMSConfig(req)
    }

    fun getBackGroundCMSConfig(@Body req: RequestUserID): Call<ResponseCmsConfigData> {
        return getAPIAtBackground().getCMSConfig(req)
    }

    fun getHsm(@Body req: UserIdRequestData): Call<ResponseXsmData> {
        return getApi().getHsm(req)
    }

    fun getLsm(@Body req: UserIdRequestData): Call<ResponseXsmData> {
        return getApi().getLsm(req)
    }

    fun getDeviceConfig(@Body req: RequestUserID): Call<ResponseDeviceConfigData> {
        return getApi().getDeviceConfig(req)
    }

    fun getMembers(@Body req: RequestUserID): Call<ResponseMemberData> {
        return getApi().getMembers(req)
    }

    fun sendBioLog(@Body req: RequestBioLog): Call<ResponseBioLog> {
        return getApi().sendBioLog(req)
    }

    fun sendUnauthorizedLeave(@Body req: RequestUnauthorizedLeave): Call<ResponseUnauthorizedLeave> {
        return getApi().sendUnauthorizedLeave(req)
    }

    fun getUserInfo(@Body req: RequestUserID): Call<ResponseUserInfoData> {
        return getApi().getUserInfo(req)
    }

    fun getBioSync(@Body req: RequestBioSyncData): Call<ResponseBioSyncData> {
        return getApi().getBioSync(req)
    }

    fun getSleepStageSync(@Body req: RequestSleepStageSyncData): Call<ResponseBioSyncData> {
        return getApi().getSleepStageSync(req)
    }

    fun getBioAnalyze(@Body req: RequestUserID): Call<ResponseBioAnalyzeData> {
        return getApi().getBioAnalyze(req)
    }

    fun getMedicalCheckUpInfo(@Body req: RequestUserID): Call<ResponseMedicalCheckUpInfoData> {
        return getApi().getMedicalCheckUpInfo(req)
    }
}