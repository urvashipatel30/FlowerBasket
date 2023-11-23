package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.ui.adapter.DashboardPagerAdapter
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityDashboardBinding
import com.flower.basket.orderflower.ui.fragment.HelpFragment
import com.flower.basket.orderflower.ui.fragment.HomeFragment
import com.flower.basket.orderflower.ui.fragment.OrdersFragment
import com.flower.basket.orderflower.ui.fragment.ReportFragment
import com.flower.basket.orderflower.ui.fragment.SettingsFragment
import com.flower.basket.orderflower.ui.fragment.SubscriptionsFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DashboardActivity : ParentActivity() {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityDashboardBinding

    private var isVendor = false
    private lateinit var viewPagerAdapter: DashboardPagerAdapter
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@DashboardActivity
        isVendor = AppPreference(activity).getPreference(AppPersistence.keys.IS_VENDOR) as Boolean

        setupPageNavigation()
        setupBackPress()
//        setTabWithPager()
    }

    private fun setupBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show()

                    // Reset the flag after a delay (e.g., 2 seconds)
                    GlobalScope.launch {
                        delay(2000)
                        doubleBackToExitPressedOnce = false
                    }
                }
            }
        }

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, callback)
    }

    /*private fun setupPageNavigation() {
        binding.bottomNavigation.apply {
            inflateMenu(if (isVendor) R.menu.bottom_vendor_item_menu else R.menu.bottom_item_menu)

            setOnItemSelectedListener { item ->
                if (isVendor) {
                    when (item.itemId) {
                        R.id.home_item -> binding.viewPager.currentItem = 0
                        R.id.report_item -> binding.viewPager.currentItem = 1
                    }
                } else {
                    when (item.itemId) {
                        R.id.subscriptions_item -> binding.viewPager.currentItem = 0
                        R.id.orders_item -> binding.viewPager.currentItem = 1
                        R.id.home_item -> binding.viewPager.currentItem = 2
                        R.id.settings_item -> binding.viewPager.currentItem = 3
                        R.id.help_item -> binding.viewPager.currentItem = 4
                    }
                }
                true
            }
        }

        viewPagerAdapter = DashboardPagerAdapter(this, isVendor)

        binding.viewPager.apply {
            offscreenPageLimit = 1
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.e("onPageSelected: ", "position => $position")
                    // Handle fragment visibility changes here
                    viewPagerAdapter.loadFragmentData(position)

                    super.onPageSelected(position)
                }
            })
            currentItem = if (isVendor) 0 else 2
        }
    }*/

    private fun setupPageNavigation() {
        binding.bottomNavigation.apply {
            inflateMenu(if (isVendor) R.menu.bottom_vendor_item_menu else R.menu.bottom_item_menu)

            setOnItemSelectedListener { item ->
                if (isVendor) {
                    when (item.itemId) {
                        R.id.home_item -> loadFragment(HomeFragment())
                        R.id.report_item -> loadFragment(ReportFragment())
                        R.id.settings_item -> loadFragment(SettingsFragment())
                    }
                } else {
                    when (item.itemId) {
                        R.id.subscriptions_item -> loadFragment(SubscriptionsFragment())
                        R.id.orders_item -> loadFragment(OrdersFragment())
                        R.id.home_item -> loadFragment(HomeFragment())
                        R.id.settings_item -> loadFragment(SettingsFragment())
                        R.id.help_item -> loadFragment(HelpFragment())
                    }
                }
                true
            }

            selectedItemId = R.id.home_item
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flHolder, fragment)
        transaction.addToBackStack(null) // Add the transaction to the back stack (optional)
        transaction.commit()
    }
}