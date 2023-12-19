package com.flower.basket.orderflower

import android.content.Context
import android.support.multidex.MultiDexApplication
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

class FlowerBasketApp: MultiDexApplication() {

    init {
        System.loadLibrary("native-lib")
    }

    companion object{
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

//        System.setProperty("javax.net.debug", "all")
        context = this.applicationContext
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)
    }
}