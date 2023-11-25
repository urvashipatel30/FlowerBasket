package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivitySplashBinding
import com.flower.basket.orderflower.ui.login.LoginActivity

class SplashActivity : ParentActivity() {

    private lateinit var activity: Activity
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@SplashActivity
        setUpDefaultData()

        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(activity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()

            val intent: Intent =
                if (AppPreference(activity).getPreference(AppPersistence.keys.IS_LOGIN) as Boolean) {
                    Intent(activity, DashboardActivity::class.java)
                } else {
                    Intent(activity, LoginActivity::class.java)
                }
            startActivity(intent)
            finish()
        }, 2500)
    }

    private fun setUpDefaultData() {
        if (AppPreference(activity).getPreference(AppPersistence.keys.IS_LOGIN) == null)
            AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, false)

        if (AppPreference(activity).getPreference(AppPersistence.keys.IS_VENDOR) == null)
            AppPreference(activity).setPreference(AppPersistence.keys.IS_VENDOR, false)

        if (AppPreference(activity).getPreference(AppPersistence.keys.USER_DATA) == null)
            AppPreference(activity).setPreference(AppPersistence.keys.USER_DATA, "")
    }
}