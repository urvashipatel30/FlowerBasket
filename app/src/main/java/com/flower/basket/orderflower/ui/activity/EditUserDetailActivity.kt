package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityEditUserDetailBinding
import com.flower.basket.orderflower.utils.PermissionUtils
import com.flower.basket.orderflower.utils.URIPathHelper
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
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
        community = binding.edtCommunity.text.toString()
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
}