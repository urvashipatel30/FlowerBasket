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
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentSettingsBinding
import com.flower.basket.orderflower.ui.activity.ChangePasswordActivity
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.EditUserDetailActivity
import com.flower.basket.orderflower.ui.activity.LoginActivity
import com.flower.basket.orderflower.views.dialog.AppAlertDialog

class SettingsFragment : ParentFragment(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity
        Log.e("onCreateView: ", "Settings activity => $activity")

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.llMyProfile.setOnClickListener(this)
        binding.llChangePassword.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.backLayout.ivBackAction -> {
                parentActivity.backToHome()
            }

            binding.llMyProfile -> {
                val intent = Intent(activity, EditUserDetailActivity::class.java)
                startActivity(intent)
            }

            binding.llChangePassword -> {
                val intent = Intent(activity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            binding.llLogout -> {
                AppAlertDialog(activity, AppAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.confirm))
                    .setContentText(getString(R.string.confirm_logout))
                    .setConfirmText(getString(R.string.logout))
                    .setConfirmClickListener { appAlertDialog ->

                        AppPreference(activity).setPreference(AppPersistence.keys.AUTH_TOKEN, "")
                        AppPreference(activity).setPreference(AppPersistence.keys.USER_DATA, "")
                        AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, false)
                        AppPreference(activity).setPreference(AppPersistence.keys.IS_VENDOR, false)

                        appAlertDialog.dismissWithAnimation()

                        val intent = Intent(activity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        activity.finish()
                    }
                    .setCancelText(activity.getString(R.string.dialog_cancel))
                    .setCancelClickListener(object : AppAlertDialog.OnDialogClickListener {
                        override fun onClick(appAlertDialog: AppAlertDialog) {
                            appAlertDialog.dismissWithAnimation()
                        }
                    })
                    .show()
            }
        }
    }
}