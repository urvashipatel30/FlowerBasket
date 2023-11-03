package com.flower.basket.orderflower.data.preference

import android.app.Activity
import android.content.Context
import com.flower.basket.orderflower.data.UserData
import com.flower.basket.orderflower.data.preference.AppPersistence.Companion.start
import com.google.gson.Gson

class AppPreference(var mContext: Context) {

    fun setPreference(name: Enum<*>?, Value: Any?) {
        start(mContext)!!.save(name!!, Value!!)
    }

    fun getPreference(name: Enum<*>?): Any? {
        return start(mContext)!![name!!]
    }

    fun removePreference(name: Enum<*>?) {
        start(mContext)!!.remove(name!!)
    }

    fun getUserDetails(): UserData? {
        if (AppPreference(mContext).getPreference(AppPersistence.keys.USER_DATA) == null)
            AppPreference(mContext).setPreference(AppPersistence.keys.USER_DATA, "")

        return if (getPreference(AppPersistence.keys.USER_DATA) != null &&
            (!(getPreference(AppPersistence.keys.USER_DATA)?.equals(""))!!)
        ) {
            Gson().fromJson(
                getPreference(AppPersistence.keys.USER_DATA).toString(),
                UserData::class.java
            )
        } else {
            null
        }
    }

    private fun <T> fromJson(jsonString: String?, theClass: Class<T>?): T {
        return gson.fromJson(jsonString, theClass)
    }

    private val gson: Gson
        get() = Gson()

    private external fun dataStr(): String
}