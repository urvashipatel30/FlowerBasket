package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.AppData
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.community.CommunityData
import com.flower.basket.orderflower.data.community.CommunityResponse
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.user.LoginRequest
import com.flower.basket.orderflower.data.user.RegistrationRequest
import com.flower.basket.orderflower.data.user.TotalUsersResponse
import com.flower.basket.orderflower.data.user.UserData
import com.flower.basket.orderflower.data.user.UserResponse
import com.flower.basket.orderflower.databinding.ActivityLoginBinding
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.UserType
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : ParentActivity(), OnClickListener, OnCheckedChangeListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityLoginBinding

    private var communityList: List<CommunityData>? = emptyList()
    private lateinit var selectedCommunity: CommunityData

    private var userId = ""
    private var userType = UserType.User.value
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

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@LoginActivity
        binding.btnSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        binding.radioGrp.setOnCheckedChangeListener(this)
        binding.radioGrpUserTypes.setOnCheckedChangeListener(this)

        if (AppPreference(activity).getPreference(AppPersistence.keys.IS_REMEMBER_USER) != null) {
            binding.cbRememberMe.isChecked =
                AppPreference(activity).getPreference(AppPersistence.keys.IS_REMEMBER_USER) as Boolean
        }

        if (AppPreference(activity).getPreference(AppPersistence.keys.USER_EMAIL) != null) {
            binding.edtLoginMail.setText(AppPreference(activity).getPreference(AppPersistence.keys.USER_EMAIL) as String)
        }

        if (AppPreference(activity).getPreference(AppPersistence.keys.USER_PASSWORD) != null) {
            binding.edtLoginPassword.setText(AppPreference(activity).getPreference(AppPersistence.keys.USER_PASSWORD) as String)
        }

        binding.cbRememberMe.setOnCheckedChangeListener { buttonView, isChecked ->

            AppPreference(activity).setPreference(AppPersistence.keys.IS_REMEMBER_USER, isChecked)

            if (isChecked) {
                AppPreference(activity).setPreference(
                    AppPersistence.keys.USER_EMAIL,
                    binding.edtLoginMail.text.toString()
                )
                AppPreference(activity).setPreference(
                    AppPersistence.keys.USER_PASSWORD,
                    binding.edtLoginPassword.text.toString()
                )
            } else {
                AppPreference(activity).setPreference(AppPersistence.keys.USER_EMAIL, "")
                AppPreference(activity).setPreference(AppPersistence.keys.USER_PASSWORD, "")
            }
        }

        setTopTab()
        getCommunityList()
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
                if (isValidFields()) {
                    registerUser()
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
                    loginUser()
                }
            }
        }
    }

    private fun loadTotalUsersList() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            RetroClient.apiService.getUsersList(AppData.allUsersURL)
                .enqueue(object : Callback<TotalUsersResponse> {
                    override fun onResponse(
                        call: Call<TotalUsersResponse>,
                        response: Response<TotalUsersResponse>
                    ) {
                        dismissLoader()
                        if (!response.isSuccessful) {
                            return
                        }
                        val totalFlowerResponse = response.body()
                    }

                    override fun onFailure(call: Call<TotalUsersResponse>, t: Throwable) {
                        dismissLoader()
                    }
                })

        }
    }

    private fun openDashboard(
        userData: UserData,
        userResponse: UserResponse
    ) {
        val json = Gson().toJson(userData)
        AppPreference(activity).setPreference(
            AppPersistence.keys.AUTH_TOKEN,
            userData.authToken
        )

        if (binding.cbRememberMe.isChecked) {
            AppPreference(activity).setPreference(
                AppPersistence.keys.USER_EMAIL,
                binding.edtLoginMail.text.toString()
            )
            AppPreference(activity).setPreference(
                AppPersistence.keys.USER_PASSWORD,
                binding.edtLoginPassword.text.toString()
            )
        } else {
            AppPreference(activity).setPreference(AppPersistence.keys.USER_EMAIL, "")
            AppPreference(activity).setPreference(AppPersistence.keys.USER_PASSWORD, "")
        }

        AppPreference(activity).setPreference(
            AppPersistence.keys.USER_DATA,
            json
        )
        AppPreference(activity).setPreference(
            AppPersistence.keys.IS_LOGIN,
            true
        )
        AppPreference(activity).setPreference(
            AppPersistence.keys.IS_VENDOR,
            userData.userType == UserType.Vendor.value
        )

        val intent = Intent(activity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginUser() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            val params = LoginRequest(email = email, password = password)

            RetroClient.apiService.loginUser(AppData.loginURL, params)
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
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
                                // Handle the retrieved user data
                                val userData = userResponse.data

//                                AppPreference(activity).setPreference(
//                                    AppPersistence.keys.AUTH_TOKEN,
//                                    userData.authToken
//                                )
//                                loadTotalUsersList()

                                openDashboard(userData, userResponse)

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

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
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

    private fun registerUser() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            val params = RegistrationRequest(
                userType = userType,
                email = email,
                userName = name,
                password = password,
                mobileNumber = mobileNumber,
                communityId = selectedCommunity.id,
                block = block,
                flatNo = flat
            )

            RetroClient.apiService.registerUser(AppData.registerURL, params)
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
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
                                // Handle the retrieved user data
                                val userData = userResponse.data

                                Toast.makeText(
                                    activity,
                                    getString(R.string.user_register_success),
                                    Toast.LENGTH_SHORT
                                ).show()

                                openDashboard(userData, userResponse)

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

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
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

    private fun getCommunityList() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            RetroClient.apiService.getCommunities(AppData.communityURL)
                .enqueue(object : Callback<CommunityResponse> {
                    override fun onResponse(
                        call: Call<CommunityResponse>,
                        response: Response<CommunityResponse>
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

                        val communityResponse = response.body()
                        if (communityResponse != null) {
                            if (communityResponse.succeeded) {
                                // Handle the retrieved community data
                                communityList = communityResponse.data

                                // Create an ArrayAdapter using list of community names
                                val communityNames = communityList!!.map { it.name }.toTypedArray()
                                val adapter = ArrayAdapter(
                                    activity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    communityNames
                                )

                                // Set the ArrayAdapter to the AutoCompleteTextView
                                binding.autoTextCommunity.setAdapter(adapter)

                                // Set an OnItemClickListener to handle item selection
                                binding.autoTextCommunity.onItemClickListener =
                                    AdapterView.OnItemClickListener { _, _, position, _ ->

                                        // Retrieve the selected community based on the selected position
                                        selectedCommunity = communityList!![position]
                                    }

                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    msg = communityResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommunityResponse>, t: Throwable) {
                        dismissLoader()
                        showDialog(
                            activity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })
        } else {
            AppAlertDialog(activity, AppAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.dialog_error_title))
                .setContentText(getString(R.string.dialog_retry_community_list))
                .setConfirmText(getString(R.string.dialog_ok))
                .setConfirmClickListener { appAlertDialog ->
                    appAlertDialog.dismissWithAnimation()
                }
                .setCancelText(getString(R.string.dialog_retry))
                .setCancelClickListener(object : AppAlertDialog.OnDialogClickListener {
                    override fun onClick(appAlertDialog: AppAlertDialog) {
                        appAlertDialog.dismissWithAnimation()
                        getCommunityList()
                    }
                })
                .show()
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
        userType = if (binding.rbUser.isChecked) UserType.User.value else UserType.Vendor.value
        name = binding.edtSignupName.text.toString()
        email = binding.edtSignupMail.text.toString()
        password = binding.edtSignupPassword.text.toString()
        mobileNumber = binding.edtMobile.text.toString()
        community = binding.autoTextCommunity.text.toString()
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
        } else if (!(communityList?.any { it.name == community }!!)) {
            showDialog(activity, msg = getString(R.string.error_community_match))
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