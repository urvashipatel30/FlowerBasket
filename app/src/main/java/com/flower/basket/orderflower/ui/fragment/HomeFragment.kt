package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
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
import com.flower.basket.orderflower.utils.NetworkUtils
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
        binding.tvUsername.text = getString(R.string.welcome_name, userDetails?.userName)

        binding.rvFlowers.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        getFlowersList()

        return binding.root
    }

    private fun getFlowersList() {
        parentActivity.showLoader(parentActivity)

        if (NetworkUtils.isNetworkAvailable(activity)) {
            RetroClient.apiService.getFlowersList()
                .enqueue(object : Callback<FlowerResponse> {
                    override fun onResponse(
                        call: Call<FlowerResponse>,
                        response: Response<FlowerResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showDialog(
                                parentActivity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val flowerResponse = response.body()
                        Log.e("onResponse: ", "flowerResponse => $flowerResponse")
                        if (flowerResponse != null) {
                            if (flowerResponse.succeeded) {
                                // Handle the retrieved flowers data
                                flowerList = flowerResponse.data as ArrayList<FlowerData>
                                Log.e("onResponse: ", "flowerList => ${flowerList.size}")

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

                            } else {
                                showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    msg = flowerResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<FlowerResponse>, t: Throwable) {
                        Log.e("onFailure: ", "error => ${t.message}")
                        showDialog(
                            parentActivity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })
        } else {
            showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }
}