package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.ui.login.data.UserData
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.UpdatePasswordRequest
import com.flower.basket.orderflower.databinding.ActivityChangePasswordBinding
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityChangePasswordBinding

    private var userDetails: UserData? = null
    private var currentPassword = ""
    private var newPassword = ""
    private var confirmPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@ChangePasswordActivity
        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        userDetails = AppPreference(activity).getUserDetails()
        currentPassword = userDetails?.password.toString()
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.backLayout.ivBackAction -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.btnSubmit -> {
                if (isValidFields()) {
                    updatePassword()
                }
            }
        }
    }

    private fun updatePassword() {
        if (userDetails?.id == null) {
            showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                msg = getString(R.string.error_went_wrong)
            )
            return
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            val params = UpdatePasswordRequest(
                currentPassword = currentPassword,
                password = newPassword
            )

            RetroClient.apiService.changePassword(userDetails?.id!!, params)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showDialog(
                                activity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val userResponse = response.body()
                        if (userResponse != null) {
                            if (userResponse.succeeded) {
                                // Update password in current stored json data
                                val userData = userDetails
                                userData?.password = newPassword

                                val json = Gson().toJson(userData)
                                AppPreference(activity).setPreference(
                                    AppPersistence.keys.USER_DATA,
                                    json
                                )

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(userResponse.message)
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()
                                        finish()
                                    }
                                    .show()

                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = userResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dismissLoader()
                        showDialog(
                            activity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })
        } else {
            showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }

    private fun isValidFields(): Boolean {
        currentPassword = binding.edtCurrentPassword.text.toString()
        newPassword = binding.edtNewPassword.text.toString()
        confirmPassword = binding.edtConfirmPassword.text.toString()

        return if (!isValidField(currentPassword)) {
            showDialog(activity, msg = getString(R.string.error_enter_curr_password))
            binding.edtCurrentPassword.requestFocus()
            false
        } else if (!isValidField(newPassword)) {
            showDialog(activity, msg = getString(R.string.error_enter_new_password))
            binding.edtNewPassword.requestFocus()
            false
        } else if (!isValidField(confirmPassword)) {
            showDialog(activity, msg = getString(R.string.error_enter_confirm_password))
            binding.edtConfirmPassword.requestFocus()
            false
        } else if (!isValidPassword(newPassword)) {
            showDialog(activity, msg = getString(R.string.error_invalid_password))
            binding.edtNewPassword.requestFocus()
            false
        } else if (newPassword != confirmPassword) {
            showDialog(activity, msg = getString(R.string.error_password_not_match))
            binding.edtConfirmPassword.requestFocus()
            false
        } else true
    }
}