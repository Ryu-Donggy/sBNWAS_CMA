package com.familidata.sbnwas_cma.common

import android.content.Context
import android.content.SharedPreferences

class ServiceManager(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences("status", Context.MODE_PRIVATE)

    fun getBoolean(key: String, default: Boolean):Boolean {
        return preferences.getBoolean(key, default)
    }

    fun setBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }
}