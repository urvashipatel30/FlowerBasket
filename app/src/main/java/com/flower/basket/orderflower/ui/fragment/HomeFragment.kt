package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.FlowerData
import com.flower.basket.orderflower.data.FlowerResponse
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentHomeBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.FlowerDetailsActivity
import com.flower.basket.orderflower.ui.adapter.FlowersListAdapter
import com.flower.basket.orderflower.ui.adapter.SubscriptionListAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.SubscriptionType
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : ParentFragment() {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentHomeBinding

    private var flowerList = ArrayList<FlowerData>()
    private lateinit var flowerAdapter: FlowersListAdapter

    private var errorMsg: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity
        Log.e("onCreateView: ", "Home activity => $activity")

        val userDetails = AppPreference(activity).getUserDetails()
        Log.e("onCreateView: ", "User id => ${userDetails?.id}")
        binding.tvUsername.text = getString(R.string.welcome_name, userDetails?.userName)

        binding.rvFlowers.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        getFlowersList()
        return binding.root
    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            rvFlowers.visibility = if (isShowList) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowList) View.GONE else View.VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    private fun getFlowersList() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(parentActivity)

            RetroClient.apiService.getFlowersList()
                .enqueue(object : Callback<FlowerResponse> {
                    override fun onResponse(
                        call: Call<FlowerResponse>,
                        response: Response<FlowerResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            errorMsg = response.message()
                            showList(false, errorMsg)
                            return
                        }

                        val flowerResponse = response.body()
                        if (flowerResponse != null) {
                            if (flowerResponse.succeeded) {
                                // Handle the retrieved flowers data
                                flowerList = flowerResponse.data as ArrayList<FlowerData>

                                if (flowerList.isNotEmpty()) {
                                    showList(true)
                                    flowerAdapter = FlowersListAdapter(
                                        activity,
                                        flowerList
                                    ) { flowerData, buyOption ->
                                        val intent = Intent(activity, FlowerDetailsActivity::class.java)
                                        intent.putExtra("buyOption", buyOption.value)
                                        intent.putExtra("data", Gson().toJson(flowerData))
                                        startActivity(intent)
                                    }
                                    binding.rvFlowers.adapter = flowerAdapter
                                } else showList(false, errorMsg)

                            } else {
                                errorMsg = flowerResponse.message
                                showList(false, errorMsg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<FlowerResponse>, t: Throwable) {
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