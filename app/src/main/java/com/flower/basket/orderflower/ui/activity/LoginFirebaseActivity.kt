package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityLoginBinding
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.flower.basket.orderflower.api.EventObserver
import com.flower.basket.orderflower.ui.viewmodel.LoginFirebaseViewModel


class LoginFirebaseActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginFirebaseViewModel

    private var mailVerificationDialog: AppAlertDialog? = null
    private var needToCheckForVerification: Boolean = false

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

        activity = this@LoginFirebaseActivity
        viewModel =
            ViewModelProvider(activity as LoginFirebaseActivity)[LoginFirebaseViewModel::class.java]
        subscribeToObservers()

        setFieldValidations()
        binding.btnSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        setTopTab()
        setUserTypes()
    }

    private fun setFieldValidations() {

        Log.e("setFieldValidations: ", "")

//        binding.edtSignupMail.addTextChangedListener (object :TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun afterTextChanged(str: Editable?) {
//                val email = str.toString()
//                Log.e("afterTextChanged: ", "email => $email")
//                if (TextUtils.isEmpty(email))
//                    binding.edtSignupMail.error = getString(R.string.error_enter_email)
//                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//                    binding.edtSignupMail.error = getString(R.string.error_invalid_email)
//                else binding.edtSignupMail.error = null
//            }
//        })
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnSignup -> {
                val email = binding.edtSignupMail.text.toString()
                Log.e("onClick: ", "email => $email")

                val password = binding.edtSignupPassword.text.toString()
                viewModel.signUpWithEmailAndPassword(email, password)
            }

            binding.btnLogin -> {
                Log.e("onClick: ", "btnLogin")
                if (binding.cbRememberMe.isChecked) {
                    AppPreference(activity).setPreference(AppPersistence.keys.IS_LOGIN, true)
                }

                val intent = Intent(activity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun isValidFields(): Boolean {
        userType = if (binding.rbUser.isChecked) 0 else 1
        name = binding.edtSignupName.text.toString()
        email = binding.edtSignupMail.text.toString()
        password = binding.edtSignupPassword.text.toString()
        mobileNumber = binding.edtMobile.text.toString()
        community = binding.edtCommunity.text.toString()
        block = binding.edtBlock.text.toString()
        flat = binding.edtFlat.text.toString()

        return if (isValidEmail(email)) true
//        else if ()
        else false
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun onResume() {
        super.onResume()

        if (needToCheckForVerification) {
            viewModel.verifyEmailAndRegister(
                userId, userType, name, email, password, community, block, flat, activationDate
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.signUpWithEmailAndPassword.observe(this, EventObserver(
            onLoading = {
                showLoader(activity)
            },
            onSuccess = { success ->
                dismissLoader()

                Log.e("signUpWithEmailAndPassword: ", "success => $success")
                if (success) {
                    needToCheckForVerification = true

                    mailVerificationDialog = AppAlertDialog(activity)
                        .setTitleText(getString(R.string.verify_email))
                        .setContentText(getString(R.string.verify_email_msg))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setConfirmClickListener {}
                    mailVerificationDialog?.show()
                }
            },
            onError = {
                dismissLoader()

                AppAlertDialog(activity, AppAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.dialog_error_title))
                    .setContentText(it)
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setConfirmClickListener{appAlertDialog ->
                        appAlertDialog.dismissWithAnimation()
                    }
                    .show()
            }
        ))

        viewModel.verifyEmailAndRegister.observe(this, EventObserver(
            onLoading = {
                showLoader(activity)
            },
            onSuccess = { success ->
                dismissLoader()
                Log.e("checkEmailVerificationStatus: ", "success => $success")
                if (success) {
                    mailVerificationDialog?.dismissWithAnimation()
                }
            },
            onError = {
                dismissLoader()
            }
        ))
    }

    private fun setUserTypes() {
        binding.radioGrpUserTypes.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbUser -> {

                }

                R.id.rbVendor -> {

                }
            }
        }
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

        binding.radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbLogin -> {
                    Log.e("setTopTab: ", "rbLogin")
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
                    Log.e("setTopTab: ", "rbSignup")
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
        }
//        binding.radioGrp.check(0)
    }
}