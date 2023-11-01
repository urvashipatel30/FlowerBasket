package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentSettingsBinding
import com.flower.basket.orderflower.ui.activity.EditUserDetailActivity
import com.flower.basket.orderflower.ui.activity.LoginActivity

class SettingsFragment : Fragment(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        activity = requireActivity()
        Log.e("onCreateView: ", "Settings activity => $activity")

        binding.llMyProfile.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.llMyProfile -> {
                val intent = Intent(activity, EditUserDetailActivity::class.java)
                startActivity(intent)
            }

            binding.llLogout -> {
                AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, false)
                AppPreference(activity).setPreference(AppPersistence.keys.IS_VENDOR, false)

                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity.finish()
            }
        }
    }
}