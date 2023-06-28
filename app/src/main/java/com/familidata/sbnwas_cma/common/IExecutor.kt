package com.familidata.sbnwas_cma.common

import android.content.Context

interface IExecutor {

    suspend fun execute(context: Context)
}