package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.order.ChangeOrderStatusRequest
import com.flower.basket.orderflower.data.report.ReportData
import com.flower.basket.orderflower.databinding.ActivityReportOrderDetailsBinding
import com.flower.basket.orderflower.ui.fragment.ReportFragment
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Quantity
import com.flower.basket.orderflower.utils.Utils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportOrderDetailsActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityReportOrderDetailsBinding

    private var reportData: ReportData? = null

    private val gramsQty = Quantity.GRAMS.value
    private val moraQty = Quantity.MORA.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@ReportOrderDetailsActivity

        if (intent != null) {
            if (intent.hasExtra("data"))
                reportData = Gson().fromJson(intent.getStringExtra("data"), ReportData::class.java)
        }

        binding.apply {
            tvFlowerName.text =
                if (reportData?.flowerTeluguName?.isNotEmpty() == true) reportData?.flowerTeluguName else reportData?.flowerName

            tvCustomerAddress.text = "${reportData?.flatNo} - ${reportData?.block}"
            tvCustomerName.text = reportData?.userName
            tvCustomerMobileNumber.text = reportData?.mobileNumber
            tvCustomerEmailID.text = reportData?.userEmail

            tvTotalQty.text = when (reportData?.flowerType) {
                FlowerType.LOOSE_FLOWER.value -> (reportData?.qty?.times(gramsQty)).toString()
                FlowerType.MORA.value -> (reportData?.qty?.times(moraQty)).toString()
                else -> (reportData?.qty?.times(gramsQty)).toString()
            }
            tvMeasurement.text =
                if (reportData?.flowerType == FlowerType.LOOSE_FLOWER.value) getString(R.string.grams)
                else getString(R.string.mora)
            tvPrice.text = getString(R.string.rupee_symbol, reportData?.totalPrice!!.toDouble())

            tvOrderDate.text = Utils().convertFromOrderDateFormat(reportData?.orderDate!!)
            tvDeliveryDate.text = Utils().convertFromOrderDateFormat(reportData?.deliveryDate!!)
        }

        if (reportData?.orderStatus == OrderStatus.DELIVERED.value) updateStatusToDelivered()

        binding.ivBackAction.setOnClickListener(this)
        binding.btnDeliveredOrder.setOnClickListener(this)
        binding.llCustomerMobileNumber.setOnClickListener(this)
        binding.llCustomerEmailID.setOnClickListener(this)

        Glide.with(binding.ivFlowerImage.context)
            .load(reportData?.flowerImageUrl)
            .placeholder(R.drawable.ic_profile_holder)
            .error(R.drawable.ic_profile_holder)
            .centerCrop()
            .into(binding.ivFlowerImage)

        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivBackAction -> onBackPressedDispatcher.onBackPressed()

            binding.btnDeliveredOrder -> {
                AppAlertDialog(activity)
                    .setTitleText(getString(R.string.dialog_confirm))
                    .setContentText(
                        getString(
                            R.string.alert_delivered_order,
                            reportData?.userName
                        )
                    )
                    .setConfirmText(getString(R.string.dialog_yes))
                    .setDialogCancelable(true)
                    .setConfirmClickListener { appAlertDialog ->
                        appAlertDialog.dismissWithAnimation()
                        changeDeliveryStatus(reportData!!)
                    }
                    .setCancelText(getString(R.string.dialog_no))
                    .setCancelClickListener(object :
                        AppAlertDialog.OnDialogClickListener {
                        override fun onClick(appAlertDialog: AppAlertDialog) {
                            appAlertDialog.dismissWithAnimation()
                        }
                    })
                    .show()
            }

            binding.llCustomerMobileNumber -> openDialer()

            binding.llCustomerEmailID -> openEmail(reportData?.userEmail!!)
        }
    }

    private fun openDialer() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${reportData?.mobileNumber}")
        startActivity(intent)
    }

    private fun changeDeliveryStatus(reportData: ReportData) {

        if (reportData?.orderId == null) {
            showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                msg = getString(R.string.error_went_wrong)
            )
            return
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            val params = ChangeOrderStatusRequest(orderStatus = OrderStatus.DELIVERED.value)

            RetroClient.apiService.changeOrderStatus(reportData.orderId, params)
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

                        val deliveredOrderResponse = response.body()

                        if (deliveredOrderResponse != null) {
                            if (deliveredOrderResponse.succeeded) {
                                // Handle the retrieved data
                                val orderID = deliveredOrderResponse.data

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.status_delivered))
                                    .setContentText(getString(R.string.success_delivered_order))
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()
                                        reportData.orderStatus = OrderStatus.DELIVERED.value
                                        updateStatusToDelivered()
                                    }
                                    .show()
                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = deliveredOrderResponse.message
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

    private fun updateStatusToDelivered() {
        binding.btnDeliveredOrder.text = getString(R.string.status_delivered)
        binding.btnDeliveredOrder.isEnabled = false
        binding.btnDeliveredOrder.alpha = 0.6f
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(
                ReportFragment.REQ_VIEW_ORDER,
                Intent().putExtra(
                    "updatedOrder",
                    Gson().toJson(reportData)
                )
            )
            finish()
        }
    }
}