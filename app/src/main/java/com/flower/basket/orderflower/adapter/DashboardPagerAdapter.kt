package com.flower.basket.orderflower.adapter

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

    override fun createFragment(position: Int): Fragment {
//        // Return a NEW fragment instance in createFragment(int).
//        val fragment = HomeFragment()
////        fragment.arguments = Bundle().apply {
////            // The object is just an integer.
////            putInt("ARG_OBJECT", position + 1)
////        }
//        return fragment

        return if (isVendor) {
            when (position) {
                0 -> HomeFragment()
                1 -> ReportFragment()
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

    override fun getItemCount(): Int = if (isVendor) 2 else 5
}