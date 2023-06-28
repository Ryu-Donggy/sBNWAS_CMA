package com.familidata.sbnwas_cma.base.model

open class PBean {
    var viewType = 0
    var bottomLineVisible = true
    var backImage: Int = 0

    companion object {
        const val TYPE_EMPTY = 0
        const val TYPE_A = 1
        const val TYPE_B = 2
        const val TYPE_BIO = 3

        const val TYPE_BLP = 1
        const val TYPE_BLS = 2
        const val TYPE_BAS_HSM = 3
        const val TYPE_BAS_LSM = 31
        const val TYPE_BDY_A = 4   //체중
        const val TYPE_BDY_B = 41  //체지방량
        const val TYPE_BDY_C = 42   //골격근량
        const val TYPE_BDY_D = 43   //체질량지수(BMI)
        const val TYPE_BDY_E = 44  //분석
        const val TYPE_BDY_F = 45  //체수분
        const val TYPE_BDY_G = 46  //체지방률
        const val TYPE_ECG = 5
        const val TYPE_HTR = 7
        const val TYPE_OXS = 9
        const val TYPE_SLEEP = 10
        const val TYPE_TMM = 11
        const val TYPE_CALORIE = 12
        const val TYPE_DISTANCE = 13
        const val TYPE_STEP = 14
        const val TYPE_ACTIVITY = 15
        const val TYPE_CHO_TC = 161  //TC,TG,HDL,LDL
        const val TYPE_CHO_TG = 162
        const val TYPE_CHO_HDL = 163
        const val TYPE_CHO_LDL = 164

        const val TYPE_ANALY_TOP = 20
        const val TYPE_ANALY_DATA = 21

        const val TYPE_DATE = 100
        const val TYPE_MONOITER_DATA = 101
        const val TYPE_WORK_DATA = 102
        const val TYPE_MONOITER_SLEEP_DATA = 103

        const val TYPE_TITLE = 200
    }
}