package com.flower.basket.orderflower

import android.support.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

class FlowerBasketApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)
    }
}