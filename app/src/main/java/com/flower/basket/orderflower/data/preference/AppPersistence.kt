package com.flower.basket.orderflower.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson

class AppPersistence private constructor(context: Context) {

    enum class keys {
        IS_LOGIN, AUTH_TOKEN, IS_VENDOR, USER_DATA, STORAGE_PATH
    }

    private val sharedPreferences: SharedPreferences
    operator fun get(key: Enum<*>): Any? {
        val all = sharedPreferences.all
        return all[key.toString()]
    }

    fun save(key: Enum<*>, `val`: Any) {
        val editor = sharedPreferences.edit()
        if (`val` is Int) {
            editor.putInt(key.toString(), `val`)
        } else if (`val` is String) {
            editor.putString(key.toString(), `val`.toString())
        } else if (`val` is Float) {
            editor.putFloat(key.toString(), `val`)
        } else if (`val` is Long) {
            editor.putLong(key.toString(), `val`)
        } else if (`val` is Boolean) {
            editor.putBoolean(key.toString(), `val`)
        } else if (`val` is Parcelable) {
            editor.putString(key.toString(), gson.toJson(`val`)).apply()
        }
        editor.apply()
    }

    val gson: Gson
        get() = Gson()

    fun remove(key: Enum<*>) {
        val editor = sharedPreferences.edit()
        editor.remove(key.toString())
        editor.apply()
    }

    fun removeAll() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val KEY = "dpix_pref"
        private var mAppPersistance: AppPersistence? = null

        @JvmStatic
        fun start(context: Context): AppPersistence? {
            if (mAppPersistance == null) {
                mAppPersistance = AppPersistence(context)
            }
            return mAppPersistance
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    }
}