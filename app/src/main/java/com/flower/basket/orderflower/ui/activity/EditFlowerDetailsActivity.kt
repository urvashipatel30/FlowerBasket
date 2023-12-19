package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.flower.FlowerData
import com.flower.basket.orderflower.data.flower.UpdateFlowerRequest
import com.flower.basket.orderflower.databinding.ActivityEditFlowerDetailsBinding
import com.flower.basket.orderflower.ui.fragment.HomeFragment
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditFlowerDetailsActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityEditFlowerDetailsBinding

    private var flowerData: FlowerData? = null
    private var flowerName = ""
    private var teluguName = ""
    private var loosePrice = 0
    private var moraPrice = 0
    private var imgURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFlowerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@EditFlowerDetailsActivity
        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("data"))
                flowerData = Gson().fromJson(intent.getStringExtra("data"), FlowerData::class.java)
        }

        binding.apply {
            edtFlowerName.setText(flowerData?.name.toString())
            edtTeluguName.setText(flowerData?.teluguName.toString())
            edtLoosePrice.setText(flowerData?.loosePrice.toString())
            edtMoraPrice.setText(flowerData?.moraPrice.toString())

            if (edtTeluguName.text.toString().isEmpty()) edtTeluguName.hint = ""
            edtFlowerName.isEnabled = false
            edtTeluguName.isEnabled = false

            edtFlowerName.alpha = resources.getInteger(R.integer.disabled_view_alpha) / 100.0f
            edtTeluguName.alpha = resources.getInteger(R.integer.disabled_view_alpha) / 100.0f
        }

        imgURL = flowerData?.imageUrl.toString()
        Glide.with(binding.ivFlowerPhoto)
            .load(flowerData?.imageUrl)
            .placeholder(R.drawable.ic_profile_holder)
            .error(R.drawable.ic_profile_holder)
            .into(binding.ivFlowerPhoto)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.backLayout.ivBackAction -> onBackPressedDispatcher.onBackPressed()

            binding.btnUpdate -> {
                if (isValidFields()) {
                    updatePassword()
                }
            }
        }
    }

    private fun updatePassword() {
        if (flowerData?.id == null) {
            showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                msg = getString(R.string.error_went_wrong)
            )
            return
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            val params = UpdateFlowerRequest(
                name = flowerName,
                teluguName = teluguName,
                loosePrice = loosePrice.toInt(),
                moraPrice = moraPrice.toInt(),
                imageUrl = imgURL
            )

            RetroClient.apiService.updateFlower(flowerData?.id!!, params)
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

                        val flowerResponse = response.body()
                        if (flowerResponse != null) {
                            if (flowerResponse.succeeded) {
                                // Update password in current stored json data
                                val flowerDataID = flowerResponse.data

                                flowerData?.name = flowerName
                                flowerData?.teluguName = teluguName
                                flowerData?.loosePrice = loosePrice
                                flowerData?.moraPrice = moraPrice

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(flowerResponse.message)
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()

                                        setResult(
                                            HomeFragment.REQ_EDIT_FLOWER,
                                            Intent().putExtra(
                                                "updatedFlower",
                                                Gson().toJson(flowerData)
                                            )
                                        )
                                        finish()
                                    }
                                    .show()

                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = flowerResponse.message
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
        flowerName = binding.edtFlowerName.text.toString()
        teluguName = binding.edtTeluguName.text.toString()
        loosePrice = binding.edtLoosePrice.text.toString().toInt()
        moraPrice = binding.edtMoraPrice.text.toString().toInt()

        return if (!isValidField(flowerName)) {
            showDialog(activity, msg = getString(R.string.error_empty_flower_name))
            binding.edtFlowerName.requestFocus()
            false
        } else if (!isValidField(loosePrice.toString())) {
            showDialog(activity, msg = getString(R.string.error_empty_loose_price))
            binding.edtLoosePrice.requestFocus()
            false
        } else if (!isValidField(moraPrice.toString())) {
            showDialog(activity, msg = getString(R.string.error_empty_mora_price))
            binding.edtMoraPrice.requestFocus()
            false
        } else true
    }
}