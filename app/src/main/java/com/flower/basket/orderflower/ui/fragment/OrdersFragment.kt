package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.databinding.FragmentHomeBinding
import com.flower.basket.orderflower.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        activity = requireActivity()
        Log.e("onCreateView: ", "Orders activity => $activity")

        return binding.root
    }
}