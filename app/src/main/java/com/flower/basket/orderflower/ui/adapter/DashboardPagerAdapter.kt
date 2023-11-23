package com.flower.basket.orderflower.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flower.basket.orderflower.ui.fragment.HelpFragment
import com.flower.basket.orderflower.ui.fragment.HomeFragment
import com.flower.basket.orderflower.ui.fragment.OrdersFragment
import com.flower.basket.orderflower.ui.fragment.ReportFragment
import com.flower.basket.orderflower.ui.fragment.SettingsFragment
import com.flower.basket.orderflower.ui.fragment.SubscriptionsFragment

class DashboardPagerAdapter(fragmentActivity: FragmentActivity, val isVendor: Boolean) :
    FragmentStateAdapter(fragmentActivity) {

//    private val fragmentLoadStatus = mutableMapOf<Int, Boolean>()

    override fun createFragment(position: Int): Fragment {
        return if (isVendor) {
            when (position) {
                0 -> HomeFragment()
                1 -> ReportFragment()
                2 -> SettingsFragment()
                else -> HomeFragment()
            }
        } else {
            when (position) {
                0 -> SubscriptionsFragment()
                1 -> OrdersFragment()
                2 -> HomeFragment()
                3 -> SettingsFragment()
                4 -> HelpFragment()
                else -> HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int = if (isVendor) 3 else 5

//    fun loadFragmentData(position: Int) {
////        val isLoaded = fragmentLoadStatus[position] ?: false
////
////        if (!isLoaded) {
//
//        when (val fragment = fragmentList[position]) {
//            is HomeFragment -> {
//                fragment.getFlowersList()
//            }
//
//            is SubscriptionsFragment -> {
//                fragment.getSubscriptionList()
//            }
//        }
////            fragmentLoadStatus[position] = true
////        }
//    }
}