package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.community.CommunityData
import com.flower.basket.orderflower.data.community.CommunityResponse
import com.flower.basket.orderflower.data.user.UpdateUserRequest
import com.flower.basket.orderflower.data.user.UserData
import com.flower.basket.orderflower.data.user.UserResponse
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityEditUserDetailBinding
import com.flower.basket.orderflower.ui.adapter.CommunityAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.PermissionUtils
import com.flower.basket.orderflower.utils.URIPathHelper
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditUserDetailActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityEditUserDetailBinding

    private var name = ""
    private var mobileNumber = ""
    private var community = ""
    private var block = ""
    private var flat = ""

    private var isVendor = false
    private var userDetails: UserData? = null

    private var communityList: List<CommunityData>? = emptyList()
    private lateinit var selectedCommunity: CommunityData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@EditUserDetailActivity
        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.ivEditProfile.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        userDetails = AppPreference(activity).getUserDetails()
        isVendor = AppPreference(activity).getPreference(AppPersistence.keys.IS_VENDOR) as Boolean

        binding.apply {
            if (isVendor) {
                llBlock.visibility = View.GONE
                llFlat.visibility = View.GONE
                autoTextCommunity.isEnabled = false
                autoTextCommunity.alpha = resources.getDimension(R.dimen.disabled_view_alpha)
            }

            edtEmailID.setText(userDetails?.email)
            edtEmailID.isEnabled = false
            edtEmailID.alpha = resources.getDimension(R.dimen.disabled_view_alpha)

            edtName.setText(userDetails?.userName)
            edtMobile.setText(userDetails?.mobileNumber?.replace("+91", "")?.trim())
            edtBlock.setText(userDetails?.block)
            edtFlat.setText(userDetails?.flatNo)
        }

        Glide.with(binding.ivEditProfile)
            .load(userDetails?.profilePhoto)
            .placeholder(R.drawable.ic_profile_holder)
            .error(R.drawable.ic_profile_holder)
            .into(binding.ivEditProfile)

        getCommunityList()
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.backLayout.ivBackAction -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.ivEditProfile -> {
                PermissionUtils.askForStorage(activity, {
                    val pickImg =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImg.type = "image/*"
                    changeImage.launch(pickImg)
                }, *arrayOf(PermissionUtils.IMAGE_PERMISSION))
            }

            binding.btnSubmit -> {
                if (isValidFields()) {
                    editUser()
                }
            }
        }
    }

    private fun editUser() {
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

            val params = UpdateUserRequest(
                userName = name,
                mobileNumber = mobileNumber,
                communityId = selectedCommunity.id,
                block = block,
                flatNo = flat
            )

            RetroClient.apiService.updateUser(userDetails?.id!!, params)
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
                                Log.e("editUser: ", "userData => $userData")

                                val json = Gson().toJson(userData)
                                AppPreference(activity).setPreference(
                                    AppPersistence.keys.USER_DATA,
                                    json
                                )

                                Toast.makeText(
                                    activity,
                                    getString(R.string.success_user_detail_updated),
                                    Toast.LENGTH_SHORT
                                ).show()

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

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data

                if (imgUri.toString().isNotEmpty()) {
                    showLoader(activity)
                    lifecycleScope.launch {
                        val path = imgUri?.let { it1 -> URIPathHelper().getPath(activity, it1) }
                        val compressedImageFile = Compressor.compress(
                            activity,
                            File(path!!)
                        )

                        Glide.with(binding.ivEditProfile)
                            .load(compressedImageFile.absolutePath)
                            .into(binding.ivEditProfile)

                        dismissLoader()
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.image_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun isValidFields(): Boolean {
        name = binding.edtName.text.toString()
        mobileNumber = binding.edtMobile.text.toString()
        community = binding.autoTextCommunity.text.toString()
        block = binding.edtBlock.text.toString()
        flat = binding.edtFlat.text.toString()

        return if (!isValidField(name)) {
            showDialog(activity, msg = getString(R.string.error_enter_name))
            binding.edtName.requestFocus()
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
            binding.autoTextCommunity.requestFocus()
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

    private fun getCommunityList() {

        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            RetroClient.apiService.getCommunities()
                .enqueue(object : Callback<CommunityResponse> {
                    override fun onResponse(
                        call: Call<CommunityResponse>,
                        response: Response<CommunityResponse>
                    ) {
                        dismissLoader()

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
                                communityList = communityResponse.data

                                val communityNames = communityList!!.map { it.name }.toTypedArray()
                                val adapter = ArrayAdapter(
                                    activity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    communityNames
                                )
                                binding.autoTextCommunity.setAdapter(adapter)

                                // Set community that got from an API
                                val community =
                                    communityList!!.find { it.id == userDetails?.communityId }
                                if (community != null) {
                                    binding.autoTextCommunity.setText(community.name)
                                } else {
                                    binding.autoTextCommunity.setText("")
                                }
                                if (community != null) {
                                    selectedCommunity = community
                                }

                                binding.autoTextCommunity.onItemClickListener =
                                    AdapterView.OnItemClickListener { _, _, _, _ ->
                                        val communityName =
                                            binding.autoTextCommunity.text.toString()
                                        selectedCommunity =
                                            communityList!!.find { it.name == communityName }!!
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
}