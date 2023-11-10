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
import com.flower.basket.orderflower.data.OrderData
import com.flower.basket.orderflower.data.OrderResponse
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentOrdersBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.adapter.OrdersListAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
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
                                // Handle the retrieved subscription data
                                ordersList =
                                    orderResponse.data as ArrayList<OrderData>
                                Log.e("onResponse: ", "order List => ${ordersList.size}")

                                if (ordersList.isNotEmpty()) {
                                    showList(true)
                                    ordersAdapter = OrdersListAdapter(
                                        activity,
                                        ordersList
                                    ) { subscriptionData ->

                                    }
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
}