package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.viewpager2.widget.ViewPager2
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.ui.adapter.DashboardPagerAdapter
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityDashboardBinding
import com.google.android.material.tabs.TabLayoutMediator


class DashboardActivity : ParentActivity() {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityDashboardBinding

    private var isVendor = false
    private lateinit var viewPagerAdapter: DashboardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@DashboardActivity
        isVendor = AppPreference(activity).getPreference(AppPersistence.keys.IS_VENDOR) as Boolean

        setupPageNavigation()
//        setTabWithPager()
    }

    private fun setupPageNavigation() {
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
        for (i in 0..viewPagerAdapter.itemCount) {
            viewPagerAdapter.createFragment(i)
        }

        binding.viewPager.apply {
            offscreenPageLimit = 1
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.e("onPageSelected: ", "position => $position")
                    if (isVendor) {
                        when (position) {
                            0 -> binding.bottomNavigation.selectedItemId = R.id.home_item
                            1 -> binding.bottomNavigation.selectedItemId = R.id.report_item
                        }
                    } else {
                        when (position) {
                            0 -> binding.bottomNavigation.selectedItemId = R.id.subscriptions_item
                            1 -> binding.bottomNavigation.selectedItemId = R.id.orders_item
                            2 -> binding.bottomNavigation.selectedItemId = R.id.home_item
                            3 -> binding.bottomNavigation.selectedItemId = R.id.settings_item
                            4 -> binding.bottomNavigation.selectedItemId = R.id.help_item
                        }
                    }
                    super.onPageSelected(position)
                }
            })
            currentItem = if (isVendor) 0 else 2
        }
    }

    private fun setTabWithPager() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabNames = listOf("Subscriptions", "Orders", "Home", "Settings", "Help")
            tab.text = tabNames[position]

            for (i in 0 until binding.tabLayout.tabCount) {
                binding.tabLayout.getTabAt(i)?.setCustomView(R.layout.tab_category_item)
                val textView =
                    binding.tabLayout.getTabAt(i)?.customView
                        ?.findViewById(R.id.tab_text) as TextView
                textView.text = tabNames[i]
                textView.setTextColor(Color.parseColor("#ffffff"))
            }

        }.attach()
    }

    private fun setTabColors() {
//        binding.tabLayout.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager))

//        for (i in 0 until binding.tabLayout.tabCount) {
//            binding.tabLayout.getTabAt(i)?.setCustomView(R.layout.tab_category_item)
//            val textView =
//                binding.tabLayout.getTabAt(i)?.getCustomView()
//                    ?.findViewById(R.id.tab_text) as TextView
//            textView.text = "All"
//            textView.setTextColor(Color.parseColor("#000000"))
//        }

//        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"))
//        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            fun onTabSelected(tab: TabLayout.Tab) {
//                Log.e("TAG", "TAB position = " + tab.getPosition())
//                binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"))
//            }
//
//            fun onTabUnselected(tab: TabLayout.Tab?) {}
//            fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
    }
}