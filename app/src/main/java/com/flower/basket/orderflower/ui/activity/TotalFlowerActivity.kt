package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.totalflowers.TotalFlowersData
import com.flower.basket.orderflower.data.totalflowers.TotalFlowersResponse
import com.flower.basket.orderflower.databinding.ActivityTotalFlowersBinding
import com.flower.basket.orderflower.ui.adapter.TotalFlowersAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalFlowerActivity : ParentActivity(), View.OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityTotalFlowersBinding

    private var date = ""
    private var flowersList = ArrayList<TotalFlowersData>()

    private lateinit var flowersAdapter: TotalFlowersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTotalFlowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@TotalFlowerActivity
        binding.backLayout.ivBackAction.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("date"))
                date = intent.getStringExtra("date").toString()
        }

        binding.rvTotalFlowers.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        loadTotalFlowersList()
    }

    private fun loadTotalFlowersList() {
        val userDetails = AppPreference(activity).getUserDetails()
        val dateToSend = Utils().convertToAPIDateFormat(date)

        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            RetroClient.apiService.getTotalFlowers(userDetails!!.communityId, dateToSend)
                .enqueue(object : Callback<TotalFlowersResponse> {
                    override fun onResponse(
                        call: Call<TotalFlowersResponse>,
                        response: Response<TotalFlowersResponse>
                    ) {
                        dismissLoader()

                        if (!response.isSuccessful) {
                            showList(false, response.message())
                            return
                        }

                        val totalFlowerResponse = response.body()

                        if (totalFlowerResponse != null) {
                            if (totalFlowerResponse.succeeded) {
                                flowersList =
                                    totalFlowerResponse.data as ArrayList<TotalFlowersData>

                                if (flowersList.isNotEmpty()) {
                                    showList(true)
                                    flowersAdapter = TotalFlowersAdapter(activity, flowersList)
                                    binding.rvTotalFlowers.adapter = flowersAdapter

                                } else showList(false, getString(R.string.error_no_flowers))
                            } else showList(false, totalFlowerResponse.message)
                        }
                    }

                    override fun onFailure(call: Call<TotalFlowersResponse>, t: Throwable) {
                        dismissLoader()
                        showList(false, t.message)
                    }
                })

        } else showList(false, getString(R.string.error_internet_msg))
    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            llFlowersList.visibility = if (isShowList) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowList) View.GONE else View.VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.backLayout.ivBackAction -> onBackPressedDispatcher.onBackPressed()
        }
    }
}