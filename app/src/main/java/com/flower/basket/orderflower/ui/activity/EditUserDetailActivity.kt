package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.CommunityData
import com.flower.basket.orderflower.data.CommunityResponse
import com.flower.basket.orderflower.data.UserData
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityEditUserDetailBinding
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.PermissionUtils
import com.flower.basket.orderflower.utils.URIPathHelper
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
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

    private lateinit var communityList: List<CommunityData>
    private lateinit var selectedCommunity: CommunityData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@EditUserDetailActivity
        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.ivEditProfile.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        isVendor = AppPreference(activity).getPreference(AppPersistence.keys.IS_VENDOR) as Boolean

        if (isVendor) {
            binding.llBlock.visibility = View.GONE
            binding.llFlat.visibility = View.GONE
        }

        userDetails = AppPreference(activity).getUserDetails()
        binding.edtEmailID.text = userDetails?.email
        binding.edtName.setText(userDetails?.userName)
        binding.edtMobile.setText(userDetails?.mobileNumber?.replace("+91", "")?.trim())
        binding.edtBlock.setText(userDetails?.block)
        binding.edtFlat.setText(userDetails?.flatNo)

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

                }
            }
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

        Log.e(
            "isValidFields: ",
            "community => ${community}, any match => ${communityList.any { it.name == community }}"
        )

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
        } else if (!communityList.any { it.name == community }) {
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

                                Log.e("onResponse: ", "communityList => ${communityResponse.data}")

                                // Create an ArrayAdapter using list of community names
                                val communityNames = communityList.map { it.name }.toTypedArray()
                                val adapter = ArrayAdapter(
                                    activity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    communityNames
                                )

                                // Set the ArrayAdapter to the AutoCompleteTextView
                                binding.autoTextCommunity.setAdapter(adapter)

                                Log.e("onResponse: ", "communityId => ${userDetails?.communityId}")
                                // Set community we got from API
                                val community =
                                    communityList.find { it.id == userDetails?.communityId }
                                if (community != null) {
                                    binding.autoTextCommunity.setText(community.name)
                                } else {
                                    binding.autoTextCommunity.setText("") // Handle the case where community is not found
                                }

                                // Set an OnItemClickListener to handle item selection
                                binding.autoTextCommunity.onItemClickListener =
                                    AdapterView.OnItemClickListener { _, _, position, _ ->

                                        // Retrieve the selected community based on the selected position
                                        selectedCommunity = communityList[position]
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
            showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }
}