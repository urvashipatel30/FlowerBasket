package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.ChangeOrderStatusRequest
import com.flower.basket.orderflower.data.OrderData
import com.flower.basket.orderflower.data.OrderResponse
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

class OrdersFragment : Fragment() {

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
        Log.e("onCreateView: ", "Orders activity => $activity")

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        getOrdersList()
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
                        Log.e("onResponse: ", "orderResponse => $orderResponse")

                        if (orderResponse != null) {
                            if (orderResponse.succeeded) {
                                // Handle the retrieved orders data
                                ordersList =
                                    orderResponse.data as ArrayList<OrderData>
                                Log.e("onResponse: ", "order List => ${ordersList.size}")

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
                        Log.e(
                            "cancelOrder: ",
                            "response => $response, ${response.isSuccessful}"
                        )

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
                        Log.e(
                            "cancelOrder: ",
                            "Response => $cancelOrderResponse"
                        )
                        Log.e(
                            "cancelOrder: ",
                            "succeeded => ${cancelOrderResponse?.succeeded}"
                        )

                        if (cancelOrderResponse != null) {
                            if (cancelOrderResponse.succeeded) {
                                // Handle the retrieved data
                                val orderID = cancelOrderResponse.data
                                Log.e("cancelOrder: ", "orderID => $orderID")

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.status_cancelled))
                                    .setContentText(getString(R.string.success_cancelled_order))
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()

                                        updateStatus(cancelOrderResponse.data, OrderStatus.CANCELED.value)
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

        // Find the item with the given ID in ordersList
        Log.e(
            "updateStatus: ",
            "itemToUpdate => $itemToUpdate"
        )

        // Update the status if the order from the list is matched
        itemToUpdate?.let {
            it.orderStatus = orderStatus // Change the status value

            // Find the position of the updated item in the list
            val updatedItemPosition = ordersList.indexOf(it)

            // Notify the adapter about the change at the specific position
            ordersAdapter.notifyItemChanged(updatedItemPosition)
        }
    }
}