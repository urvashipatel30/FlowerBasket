package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.flower.basket.orderflower.api.AppData
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivitySplashBinding

class SplashActivity : ParentActivity() {

    private lateinit var activity: Activity
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@SplashActivity
        encryptAppStrings()
        setUpDefaultData()

        Handler(Looper.getMainLooper()).postDelayed({
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

        if (AppPreference(activity).getPreference(AppPersistence.keys.AUTH_TOKEN) == null)
            AppPreference(activity).setPreference(AppPersistence.keys.AUTH_TOKEN, "")
    }

    private fun encryptAppStrings() {
        Log.e("encryptAppStrings: ", "Community GetAll => ${AppData.scrambleData("api/Community/GetAll")}")
        Log.e("encryptAppStrings: ", "register => ${AppData.scrambleData("api/Users/register")}")
        Log.e("encryptAppStrings: ", "login => ${AppData.scrambleData("api/Users/login")}")
        Log.e("encryptAppStrings: ", "Users GetAll => ${AppData.scrambleData("api/Users/GetAll")}")
        Log.e("encryptAppStrings: ", "Users Update => ${AppData.scrambleData("api/Users/Update/{id}")}")
        Log.e("encryptAppStrings: ", "ChangePassword => ${AppData.scrambleData("api/Users/ChangePassword/{id}")}")

        Log.e("encryptAppStrings: ", "Flowers GetAll => ${AppData.scrambleData("api/Flowers/GetAll")}")

        Log.e("encryptAppStrings: ", "Subscriptions Add => ${AppData.scrambleData("api/Subscriptions/Add")}")
        Log.e("encryptAppStrings: ", "Subscriptions GetAll => ${AppData.scrambleData("api/Subscriptions/GetAll/{id}")}")
        Log.e("encryptAppStrings: ", "Subscriptions Get => ${AppData.scrambleData("api/Subscriptions/Get/{id}")}")
        Log.e("encryptAppStrings: ", "Subscriptions Update => ${AppData.scrambleData("api/Subscriptions/Update/{id}")}")
        Log.e("encryptAppStrings: ", "Subscriptions ManageVacationMode => ${AppData.scrambleData("api/Subscriptions/ManageVacationMode/{id}")}")
        Log.e("encryptAppStrings: ", "Subscriptions Delete => ${AppData.scrambleData("api/Subscriptions/Delete/{id}")}")

        Log.e("encryptAppStrings: ", "Order GetAll => ${AppData.scrambleData("api/Order/GetAll/{id}")}")
        Log.e("encryptAppStrings: ", "Order GenerateOrder => ${AppData.scrambleData("api/Order/GenerateOrder")}")
        Log.e("encryptAppStrings: ", "Order UpdateOrderStatus => ${AppData.scrambleData("api/Order/UpdateOrderStatus/{id}")}")

        Log.e("encryptAppStrings: ", "Vendor GetVendorByCommunity => ${AppData.scrambleData("api/Vendor/GetVendorByCommunity/{id}")}")
        Log.e("encryptAppStrings: ", "Flowers Update => ${AppData.scrambleData("api/Flowers/Update/{id}")}")
        Log.e("encryptAppStrings: ", "Vendor GetAllOrders => ${AppData.scrambleData("api/Vendor/GetAllOrders/{communityId}")}")
        Log.e("encryptAppStrings: ", "Vendor GetTotalFlowers => ${AppData.scrambleData("api/Vendor/GetTotalFlowers/{communityId}")}")
    }
}