package com.flower.basket.orderflower.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flower.basket.orderflower.fragment.HelpFragment
import com.flower.basket.orderflower.fragment.HomeFragment
import com.flower.basket.orderflower.fragment.OrdersFragment
import com.flower.basket.orderflower.fragment.SettingsFragment
import com.flower.basket.orderflower.fragment.SubscriptionsFragment

class DashboardPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    //    private const val ARG_OBJECT = "object"
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
//        // Return a NEW fragment instance in createFragment(int).
//        val fragment = HomeFragment()
////        fragment.arguments = Bundle().apply {
////            // The object is just an integer.
////            putInt("ARG_OBJECT", position + 1)
////        }
//        return fragment

        return when (position) {
            0 -> SubscriptionsFragment()
            1 -> OrdersFragment()
            2 -> HomeFragment()
            3 -> SettingsFragment()
            else -> HelpFragment()
        }
    }
}