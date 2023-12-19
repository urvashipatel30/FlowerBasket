package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.order.ChangeOrderStatusRequest
import com.flower.basket.orderflower.data.order.OrderData
import com.flower.basket.orderflower.data.order.OrderResponse
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentOrdersBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.adapter.OrdersListAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : ParentFragment() {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentOrdersBinding

    private var ordersList = ArrayList<OrderData>()
    private lateinit var ordersAdapter: OrdersListAdapter

    private var errorMsg: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        binding.backLayout.ivBackAction.setOnClickListener {
            parentActivity.backToHome()
        }

        generateOrder()
        return binding.root
    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            rvOrders.visibility = if (isShowList) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowList) View.GONE else View.VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    private fun getOrdersList() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(parentActivity)

            val userDetails = AppPreference(activity).getUserDetails()

            RetroClient.apiService.getAllOrders(userDetails?.id.toString())
                .enqueue(object : Callback<OrderResponse> {
                    override fun onResponse(
                        call: Call<OrderResponse>,
                        response: Response<OrderResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            errorMsg = response.message()
                            showList(false, errorMsg)
                            return
                        }

                        val orderResponse = response.body()

                        if (orderResponse != null) {
                            if (orderResponse.succeeded) {
                                // Handle the retrieved orders data
                                ordersList =
                                    orderResponse.data as ArrayList<OrderData>

                                if (ordersList.isNotEmpty()) {
                                    showList(true)
                                    ordersAdapter = OrdersListAdapter(
                                        activity,
                                        ordersList,
                                        onItemSelected = { orderData ->

                                        },
                                        onOrderCancelled = { orderData ->
                                            AppAlertDialog(activity)
                                                .setTitleText(getString(R.string.dialog_confirm))
                                                .setContentText(getString(R.string.alert_cancel_order))
                                                .setConfirmText(getString(R.string.dialog_yes))
                                                .setDialogCancelable(true)
                                                .setConfirmClickListener { appAlertDialog ->
                                                    appAlertDialog.dismissWithAnimation()
                                                    cancelOrder(orderData)
                                                }
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setCancelClickListener(object :
                                                    AppAlertDialog.OnDialogClickListener {
                                                    override fun onClick(appAlertDialog: AppAlertDialog) {
                                                        appAlertDialog.dismissWithAnimation()
                                                    }
                                                })
                                                .show()
                                        })
                                    binding.rvOrders.adapter = ordersAdapter

                                } else {
                                    errorMsg = getString(R.string.error_no_orders)
                                    showList(false, errorMsg)
                                }

                            } else {
                                errorMsg = orderResponse.message
                                showList(false, errorMsg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
                        errorMsg = t.message
                        showList(false, errorMsg)
                    }
                })
        } else {
            errorMsg = getString(R.string.error_internet_msg)
            showList(false, errorMsg)
        }
    }

    private fun generateOrder() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            RetroClient.apiService.generateOrders()
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        if (!response.isSuccessful) {
                            parentActivity.dismissLoader()
//                            parentActivity.showDialog(
//                                activity,
//                                dialogType = AppAlertDialog.ERROR_TYPE,
//                                msg = response.message() ?: getString(R.string.error_went_wrong)
//                            )
                        } else {
                            val generateOrderResponse = response.body()
                            if (generateOrderResponse != null) {

                                if (generateOrderResponse.succeeded) {
//                                parentActivity.showDialog(
//                                    activity,
//                                    dialogType = AppAlertDialog.SUCCESS_TYPE,
//                                    msg = generateOrderResponse.message
//                                )
                                } else {
                                    parentActivity.dismissLoader()
//                                    parentActivity.showDialog(
//                                        activity,
//                                        dialogType = AppAlertDialog.ERROR_TYPE,
//                                        title = getString(R.string.failed),
//                                        msg = generateOrderResponse.message
//                                    )
                                }
                            }
                        }
                        getOrdersList()
                    }

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
//                        parentActivity.showDialog(
//                            activity,
//                            dialogType = AppAlertDialog.ERROR_TYPE,
//                            msg = t.message ?: getString(R.string.error_went_wrong)
//                        )
                    }
                })
        } else {
            parentActivity.showDialog(
                activity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }

    private fun cancelOrder(orderData: OrderData) {

        if (orderData?.id == null) {
            parentActivity.showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                msg = getString(R.string.error_went_wrong)
            )
            return
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            val params = ChangeOrderStatusRequest(orderStatus = OrderStatus.CANCELED.value)

            RetroClient.apiService.changeOrderStatus(orderData?.id!!, params)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            parentActivity.showDialog(
                                parentActivity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val cancelOrderResponse = response.body()

                        if (cancelOrderResponse != null) {
                            if (cancelOrderResponse.succeeded) {
                                // Handle the retrieved data
                                val orderID = cancelOrderResponse.data

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.status_cancelled))
                                    .setContentText(getString(R.string.success_cancelled_order))
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()

                                        updateStatus(
                                            cancelOrderResponse.data,
                                            OrderStatus.CANCELED.value
                                        )
                                    }
                                    .show()
                            } else {
                                parentActivity.showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = cancelOrderResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
                        parentActivity.showDialog(
                            parentActivity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })
        } else {
            parentActivity.showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }

    private fun updateStatus(orderId: String?, orderStatus: Int) {
        val itemToUpdate = ordersList.find { it.id == orderId }

        // Update the status if the order from the list is matched
        itemToUpdate?.let {
            it.orderStatus = orderStatus

            val updatedItemPosition = ordersList.indexOf(it)
            ordersAdapter.notifyItemChanged(updatedItemPosition)
        }
    }
}