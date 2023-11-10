package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.DeleteSubscriptionResponse
import com.flower.basket.orderflower.data.SubscriptionListData
import com.flower.basket.orderflower.data.SubscriptionListResponse
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentSubscriptionsBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.EditSubscriptionActivity
import com.flower.basket.orderflower.ui.adapter.SubscriptionListAdapter
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionsFragment : ParentFragment() {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentSubscriptionsBinding

    private var subscriptionList = ArrayList<SubscriptionListData>()
    private var filteredSubscriptionList: ArrayList<SubscriptionListData> =
        ArrayList<SubscriptionListData>()
    private lateinit var subscriptionAdapter: SubscriptionListAdapter

    private var errorMsg: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity
        Log.e("onCreateView: ", "Subscriptions activity => $activity")

        binding.rvSubscriptions.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        getSubscriptionList()
        return binding.root
    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            rvSubscriptions.visibility = if (isShowList) VISIBLE else GONE
            llDataErrorView.visibility = if (isShowList) GONE else VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    private fun getSubscriptionList() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(parentActivity)

            val userDetails = AppPreference(activity).getUserDetails()

            RetroClient.apiService.getAllSubscriptions(userDetails?.id.toString())
                .enqueue(object : Callback<SubscriptionListResponse> {
                    override fun onResponse(
                        call: Call<SubscriptionListResponse>,
                        response: Response<SubscriptionListResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            errorMsg = response.message()
                            showList(false, errorMsg)
                            return
                        }

                        val subscriptionResponse = response.body()
                        Log.e("onResponse: ", "subscriptionResponse => $subscriptionResponse")

                        if (subscriptionResponse != null) {
                            if (subscriptionResponse.succeeded) {
                                // Handle the retrieved subscription data
                                subscriptionList =
                                    subscriptionResponse.data as ArrayList<SubscriptionListData>
                                Log.e(
                                    "onResponse: ",
                                    "subscription List => ${subscriptionList.size}"
                                )

                                filteredSubscriptionList =
                                    ArrayList(subscriptionList.filter { it.subscriptionType == 1 })

                                if (filteredSubscriptionList.isNotEmpty()) {
                                    showList(true)
                                    subscriptionAdapter = SubscriptionListAdapter(
                                        activity,
                                        filteredSubscriptionList,
                                        onItemSelected = { subscriptionData ->
                                            val intent = Intent(
                                                activity,
                                                EditSubscriptionActivity::class.java
                                            )
                                            intent.putExtra("subscriptionID", subscriptionData.id)
                                            intent.putExtra("data", Gson().toJson(subscriptionData))
                                            startActivity(intent)
                                        },
                                        onItemDeleted = { subscriptionData ->

                                            AppAlertDialog(activity, AppAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.dialog_warning))
                                                .setContentText(getString(R.string.warning_delete_subscription))
                                                .setConfirmText(getString(R.string.dialog_yes))
                                                .setDialogCancelable(true)
                                                .setConfirmClickListener { appAlertDialog ->
                                                    appAlertDialog.dismissWithAnimation()
                                                    deleteSubscription(subscriptionData)
                                                }
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setCancelClickListener(object :
                                                    AppAlertDialog.OnSweetClickListener {
                                                    override fun onClick(appAlertDialog: AppAlertDialog) {
                                                        appAlertDialog.dismissWithAnimation()
                                                    }
                                                })
                                                .show()
                                        })

                                    binding.rvSubscriptions.adapter = subscriptionAdapter
                                } else {
                                    errorMsg = getString(R.string.error_no_subscriptions)
                                    showList(false, errorMsg)
                                }

                            } else {
                                errorMsg = subscriptionResponse.message
                                showList(false, errorMsg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SubscriptionListResponse>, t: Throwable) {
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

    private fun deleteSubscription(subscriptionData: SubscriptionListData) {

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            if (subscriptionData?.id == null) {
                parentActivity.dismissLoader()
                showDialog(
                    parentActivity,
                    dialogType = AppAlertDialog.ERROR_TYPE,
                    msg = getString(R.string.error_went_wrong)
                )
                return
            }

            RetroClient.apiService.deleteSubscription(subscriptionData?.id!!)
                .enqueue(object : Callback<DeleteSubscriptionResponse> {
                    override fun onResponse(
                        call: Call<DeleteSubscriptionResponse>,
                        response: Response<DeleteSubscriptionResponse>
                    ) {
                        parentActivity.dismissLoader()
                        Log.e(
                            "deletesubscriptionResponse: ",
                            "response => $response, ${response.isSuccessful}"
                        )

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showDialog(
                                parentActivity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val deletesubscriptionResponse = response.body()
                        Log.e(
                            "deletesubscriptionResponse: ",
                            "Response => $deletesubscriptionResponse"
                        )
                        Log.e(
                            "deletesubscriptionResponse: ",
                            "succeeded => ${deletesubscriptionResponse?.succeeded}"
                        )

                        if (deletesubscriptionResponse != null) {
                            if (deletesubscriptionResponse.succeeded) {
                                // Handle the retrieved user data
                                val subscriptionID = deletesubscriptionResponse.data
                                Log.e("updateSubscriptions: ", "subscriptionID => $subscriptionID")

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success_deleted))
                                    .setContentText(deletesubscriptionResponse.message)
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()

                                        val positionToRemove =
                                            filteredSubscriptionList.indexOfFirst { it.id == deletesubscriptionResponse.data }
                                        // Find the position of the item to be removed
                                        Log.e(
                                            "onResponse: ",
                                            "positionToRemove => $positionToRemove"
                                        )
                                        if (positionToRemove != -1) {
                                            // Remove the item from the list
                                            filteredSubscriptionList.removeAt(positionToRemove)

                                            // Notify the adapter of the removed item
                                            subscriptionAdapter.notifyItemRemoved(positionToRemove)
                                        }

                                        Log.e(
                                            "onResponse: ",
                                            "subscriptionList => ${filteredSubscriptionList.size}"
                                        )

                                        if (filteredSubscriptionList.isEmpty()) showList(
                                            false,
                                            getString(R.string.error_no_subscriptions)
                                        )
                                    }
                                    .show()
                            } else {
                                showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = deletesubscriptionResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<DeleteSubscriptionResponse>, t: Throwable) {
                        parentActivity.dismissLoader()

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