package com.familidata.sbnwas_cma.api

import com.familidata.sbnwas_cma.api.req.*
import com.familidata.sbnwas_cma.api.res.*
import retrofit2.Call
import retrofit2.http.*

interface ApiCall {

    @POST("/api/user/check/")
    fun getLogin(@Body requestLoginData: RequestLoginData): Call<ResponseLoginData>


    @POST("/api/sbnwas/members/get")
    fun getUserInfo(@Body mRequestUserID: RequestUserID): Call<ResponseUserInfoData>

    @POST("/api/sbnwas/work/plan/")
    fun getWorkPlan(@Body req: UserIdRequestData): Call<ResponseWorkPlanData>

    @POST("/api/sbnwas/work/execution/")
    fun postWorkStatusExecution(@Body req: RequestStatusData): Call<ResponseWorkStatusData>

    @POST("/api/sbnwas/work/comment/")
    fun postWorkDiary(@Body req: RequestCommentData): Call<ResponseWorkStatusData>

    @POST("/api/sbnwas/config/cma")
    fun getCMSConfig(@Body req: RequestUserID): Call<ResponseCmsConfigData>


    @POST("/api/sbnwas/bio/hsm")
    fun getHsm(@Body req: UserIdRequestData): Call<ResponseXsmData>

    @POST("/api/sbnwas/bio/lsm")
    fun getLsm(@Body req: UserIdRequestData): Call<ResponseXsmData>

    @POST("/api/sbnwas/config/device")
    fun getDeviceConfig(@Body req: RequestUserID): Call<ResponseDeviceConfigData>

    @POST("/api/sbnwas/members/ship")
    fun getMembers(@Body req: RequestUserID): Call<ResponseMemberData>

    @POST("/api/fdss/sbnwas/producer")
    fun sendBioLog(@Body req: RequestBioLog): Call<ResponseBioLog>

    @POST("/api/fdss/sbnwas/producer")
    fun sendUnauthorizedLeave(@Body req: RequestUnauthorizedLeave): Call<ResponseUnauthorizedLeave>

    @POST("/api/sbnwas/bio/sync")
    fun getBioSync(@Body req: RequestBioSyncData): Call<ResponseBioSyncData>

    @POST("/api/sbnwas/bio/sync/sleepstage")
    fun getSleepStageSync(@Body req: RequestSleepStageSyncData): Call<ResponseBioSyncData>

    @POST("/api/sbnwas/health/report/get")
    fun getBioAnalyze(@Body req: RequestUserID): Call<ResponseBioAnalyzeData>

    @POST("/api/sbnwas/health/check")
    fun getMedicalCheckUpInfo(@Body req: RequestUserID): Call<ResponseMedicalCheckUpInfoData>
}