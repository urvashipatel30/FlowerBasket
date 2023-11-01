package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentSettingsBinding
import com.flower.basket.orderflower.databinding.FragmentSubscriptionsBinding

class SubscriptionsFragment : Fragment(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentSubscriptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)

        activity = requireActivity()
        Log.e("onCreateView: ", "Subscriptions activity => $activity")

        showSelectedPlan()
        return binding.root
    }

    private fun showSelectedPlan() {

    }

    override fun onClick(view: View?) {

        when (view) {

        }
    }
}