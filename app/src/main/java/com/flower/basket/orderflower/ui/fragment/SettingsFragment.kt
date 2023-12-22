package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.AppData
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.report.ReportResponse
import com.flower.basket.orderflower.databinding.FragmentSettingsBinding
import com.flower.basket.orderflower.ui.activity.ChangePasswordActivity
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.EditUserDetailActivity
import com.flower.basket.orderflower.ui.activity.LoginActivity
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.llMyProfile.setOnClickListener(this)
        binding.llChangePassword.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)
        binding.llDeleteAccount.setOnClickListener(this)

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

            binding.llDeleteAccount -> {
                AppAlertDialog(activity, AppAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.delete_account))
                    .setContentText(getString(R.string.confirm_delete_account))
                    .setConfirmText(getString(R.string.delete))
                    .setConfirmClickListener { appAlertDialog ->
                        deleteUser()
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

    private fun deleteUser() {
        val userDetails = AppPreference(activity).getUserDetails()

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            RetroClient.apiService.deleteUser("${AppData.vendorReportOrdersURL}/${userDetails?.id!!}")
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        parentActivity.dismissLoader()

                        if (!response.isSuccessful) {
                            showDialog(
                                parentActivity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val deleteUserResponse = response.body()
                        if (deleteUserResponse != null) {

                            if (deleteUserResponse.succeeded) {
                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(deleteUserResponse.message)
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->

                                        AppPreference(activity).setPreference(AppPersistence.keys.AUTH_TOKEN, "")
                                        AppPreference(activity).setPreference(AppPersistence.keys.USER_DATA, "")
                                        AppPreference(activity).setPreference(AppPersistence.keys.USER_EMAIL, "")
                                        AppPreference(activity).setPreference(AppPersistence.keys.USER_PASSWORD, "")
                                        AppPreference(activity).setPreference(AppPersistence.keys.IS_REMEMBER_USER, false)
                                        AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, false)
                                        AppPreference(activity).setPreference(AppPersistence.keys.IS_VENDOR, false)

                                        appAlertDialog.dismissWithAnimation()

                                        val intent = Intent(activity, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        activity.finish()
                                    }
                                    .show()
                            } else {
                                showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = deleteUserResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
                        showDialog(
                            parentActivity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })

        } else {
            showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }
}