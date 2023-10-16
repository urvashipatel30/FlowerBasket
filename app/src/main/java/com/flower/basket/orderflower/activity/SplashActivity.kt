package com.flower.basket.orderflower.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.flower.basket.orderflower.databinding.ActivitySplashBinding

class SplashActivity : ParentActivity() {
    private lateinit var activity: Activity
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@SplashActivity

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}