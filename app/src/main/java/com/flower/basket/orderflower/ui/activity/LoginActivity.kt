package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.core.content.ContextCompat
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.network.RetroClient
import com.flower.basket.orderflower.data.DesignResponse
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityLoginBinding
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : ParentActivity(), OnClickListener, OnCheckedChangeListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityLoginBinding

    private var userId = ""
    private var userType = 1
    private var name = ""
    private var email = ""
    private var password = ""
    private var mobileNumber = ""
    private var community = ""
    private var block = ""
    private var flat = ""
    private var activationDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@LoginActivity
        binding.btnSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        binding.radioGrp.setOnCheckedChangeListener(this)
        binding.radioGrpUserTypes.setOnClickListener(this)

        setTopTab()
    }

    private fun setTopTab() {
        binding.rbLogin.setTextColor(
            ContextCompat.getColor(
                activity,
                R.color.white
            )
        )
        binding.rbSignup.setTextColor(
            ContextCompat.getColor(
                activity,
                R.color.unselectedLoginTabTextColor
            )
        )
//        binding.radioGrp.check(0)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnSignup -> {
                // Trigger API call if entered valid fields
                if (isValidFields()) {
                    RetroClient.apiService.getPostById()
                        .enqueue(object : Callback<DesignResponse> {
                            override fun onResponse(
                                call: Call<DesignResponse>,
                                response: Response<DesignResponse>
                            ) {
                                Log.d("onResponse: ", "response => $response")

                                // if response of registration is not successful
                                if (!response.isSuccessful) {
                                    Log.e(
                                        "onResponse: ",
                                        "response not Successful => ${response.message()}"
                                    )
                                    showDialog(
                                        activity,
                                        dialogType = AppAlertDialog.ERROR_TYPE,
                                        msg = response.message() ?: getString(R.string.went_wrong)
                                    )
                                    return
                                }

                                val userResponse = response.body()
                                Log.e("onResponse: ", "userResponse => $userResponse")

                                if (userResponse != null) {
                                    if (userResponse.succeeded) {
                                        // Handle the retrieved post data
                                        AppPreference(activity).setPreference(
                                            AppPersistence.keys.IS_VENDOR,
                                            true
                                        )
                                        AppPreference(activity).setPreference(
                                            AppPersistence.keys.IS_LOGIN,
                                            true
                                        )

                                        val intent = Intent(activity, DashboardActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    } else {
                                        showDialog(
                                            activity,
                                            dialogType = AppAlertDialog.ERROR_TYPE,
                                            msg = userResponse.messages
                                        )
                                    }
                                }
                            }

                            override fun onFailure(call: Call<DesignResponse>, t: Throwable) {
                                // Handle failure
                                Log.e("onFailure: ", "error => ${t.message}")
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    msg = t.message ?: getString(R.string.went_wrong)
                                )
                            }
                        })
                }
            }

            binding.btnLogin -> {
                email = binding.edtLoginMail.text.toString()
                password = binding.edtLoginPassword.text.toString()

                if (!isValidEmail(email)) {
                    binding.edtLoginMail.requestFocus()
                } else if (!isValidField(password)) {
                    showDialog(activity, msg = getString(R.string.error_enter_password))
                    binding.edtLoginPassword.requestFocus()
                } else if (!isValidPassword(password)) {
                    showDialog(activity, msg = getString(R.string.error_invalid_password))
                    binding.edtSignupPassword.requestFocus()
                } else {
//                    if (binding.cbRememberMe.isChecked) {
//                        AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, true)
//                    }

                    AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, true)
                    AppPreference(activity).setPreference(AppPersistence.keys.IS_VENDOR, false)

                    val intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onCheckedChanged(view: RadioGroup?, checkedId: Int) {
        when (view) {
            binding.radioGrp -> when (checkedId) {
                R.id.rbLogin -> {
                    binding.llLogin.visibility = View.VISIBLE
                    binding.llSignup.visibility = View.GONE

                    binding.rbLogin.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                    binding.rbSignup.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.unselectedLoginTabTextColor
                        )
                    )
                }

                R.id.rbSignup -> {
                    binding.llLogin.visibility = View.GONE
                    binding.llSignup.visibility = View.VISIBLE

                    binding.rbLogin.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.unselectedLoginTabTextColor
                        )
                    )
                    binding.rbSignup.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }
            }

            binding.radioGrpUserTypes -> when (checkedId) {
                R.id.rbUser -> {
                    binding.edtBlock.visibility = View.VISIBLE
                    binding.edtFlat.visibility = View.VISIBLE
                }

                R.id.rbVendor -> {
                    binding.edtBlock.visibility = View.GONE
                    binding.edtFlat.visibility = View.GONE
                }
            }

        }
    }

    private fun isValidFields(): Boolean {
        userType = if (binding.rbUser.isChecked) 0 else 1
        name = binding.edtSignupName.text.toString()
        email = binding.edtSignupMail.text.toString()
        password = binding.edtSignupPassword.text.toString()
        mobileNumber = binding.edtMobile.text.toString()
        community = binding.edtCommunity.text.toString()
        block = binding.edtBlock.text.toString()
        flat = binding.edtFlat.text.toString()

        return if (!isValidField(name)) {
            showDialog(activity, msg = getString(R.string.error_enter_name))
            binding.edtSignupName.requestFocus()
            false
        } else if (!isValidEmail(email)) {
            binding.edtSignupMail.requestFocus()
            false
        } else if (!isValidField(password)) {
            showDialog(activity, msg = getString(R.string.error_enter_password))
            binding.edtSignupPassword.requestFocus()
            false
        } else if (!isValidPassword(password)) {
            showDialog(activity, msg = getString(R.string.error_invalid_password))
            binding.edtSignupPassword.requestFocus()
            false
        } else if (!isValidField(mobileNumber)) {
            showDialog(activity, msg = getString(R.string.error_enter_mobileNumber))
            binding.edtMobile.requestFocus()
            false
        } else if (!isValidMobileNumber(mobileNumber)) {
            showDialog(activity, msg = getString(R.string.error_invalid_mobileNumber))
            binding.edtMobile.requestFocus()
            false
        } else if (!isValidField(community)) {
            showDialog(activity, msg = getString(R.string.error_enter_community))
            binding.edtCommunity.requestFocus()
            false
        } else if (!isValidField(block)) {
            showDialog(activity, msg = getString(R.string.error_enter_block))
            binding.edtBlock.requestFocus()
            false
        } else if (!isValidField(flat)) {
            showDialog(activity, msg = getString(R.string.error_enter_flat))
            binding.edtFlat.requestFocus()
            false
        } else true
    }

    private fun isValidPassword(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && target?.length!! >= 6
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        if (TextUtils.isEmpty(target)) {
            showDialog(activity, msg = getString(R.string.error_enter_email))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            showDialog(activity, msg = getString(R.string.error_invalid_email))
            return false
        }
        return true
    }
}