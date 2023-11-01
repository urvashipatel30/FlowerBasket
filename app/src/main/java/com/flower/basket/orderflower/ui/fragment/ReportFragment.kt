package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.databinding.FragmentHelpBinding
import com.flower.basket.orderflower.databinding.FragmentReportBinding

class ReportFragment : Fragment() {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        activity = requireActivity()
        Log.e("onCreateView: ", "Report activity => $activity")

        return binding.root
    }
}