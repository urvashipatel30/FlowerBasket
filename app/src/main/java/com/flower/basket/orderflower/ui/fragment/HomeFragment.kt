package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.databinding.FragmentHomeBinding
import com.flower.basket.orderflower.ui.activity.FlowerDetailsActivity

class HomeFragment : Fragment() {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        activity = requireActivity()
        Log.e("onCreateView: ", "Home activity => $activity")

        binding.flowerItem.cardItem.setOnClickListener {
            val intent = Intent(activity, FlowerDetailsActivity::class.java)
            intent.putExtra("buyOption", 0)
            startActivity(intent)
        }

        binding.flowerItem.btnSubscribe.setOnClickListener {
            val intent = Intent(activity, FlowerDetailsActivity::class.java)
            intent.putExtra("buyOption", 0)
            startActivity(intent)
        }

        binding.flowerItem.btnBuyOnce.setOnClickListener {
            val intent = Intent(activity, FlowerDetailsActivity::class.java)
            intent.putExtra("buyOption", 1)
            startActivity(intent)
        }

        return binding.root
    }
}