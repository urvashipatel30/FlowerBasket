package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.AppData
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.order.ChangeOrderStatusRequest
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.report.ReportData
import com.flower.basket.orderflower.data.report.ReportDataToSend
import com.flower.basket.orderflower.data.report.ReportResponse
import com.flower.basket.orderflower.databinding.FragmentReportBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.ReportOrderDetailsActivity
import com.flower.basket.orderflower.ui.activity.TotalFlowerActivity
import com.flower.basket.orderflower.ui.adapter.ReportListAdapter
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Utils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import org.json.CDL
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ReportFragment : ParentFragment(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentReportBinding

    private var reportListJSON: String? = null
    private var reportList = ArrayList<ReportData>()

    //    private var reportListToSend: List<ReportDataToSend> = emptyList()
    //    private var reportListToSend: List<LinkedHashMap<String, Any>> = emptyList()
    private lateinit var reportAdapter: ReportListAdapter

    private val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)

    companion object {
        const val REQ_VIEW_ORDER = 5
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity

        binding.tvSelectedDate.text = getFormattedCurrentDate()

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.llChooseDate.setOnClickListener(this)
        binding.ivDownloadReport.setOnClickListener(this)
        binding.btnTotalFlower.setOnClickListener(this)

        binding.rvReport.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        generateOrder()
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.backLayout.ivBackAction -> parentActivity.backToHome()
            binding.llChooseDate -> showDatePicker()
            binding.ivDownloadReport -> {
//                launchBaseDirectoryPicker()
                saveToCSVFile()
            }

            binding.btnTotalFlower -> {
                val intent = Intent(activity, TotalFlowerActivity::class.java)
                intent.putExtra("date", binding.tvSelectedDate.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun loadReport() {
        val selectedDate = binding.tvSelectedDate.text.toString()

        val userDetails = AppPreference(activity).getUserDetails()
        val dateToSend = Utils().convertToAPIDateFormat(selectedDate)

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            RetroClient.apiService.getReport(
                "${AppData.vendorReportOrdersURL}/${userDetails?.communityId!!}",
                dateToSend
            )
                .enqueue(object : Callback<ReportResponse> {
                    override fun onResponse(
                        call: Call<ReportResponse>,
                        response: Response<ReportResponse>
                    ) {
                        parentActivity.dismissLoader()

                        if (!response.isSuccessful) {
                            showList(false, response.message())
                            return
                        }

                        val reportResponse = response.body()

                        if (reportResponse != null) {
                            if (reportResponse.succeeded) {
                                reportListJSON =
                                    reportResponse.data.toString().replace("ReportData", "")

                                reportList = reportResponse.data as ArrayList<ReportData>

                                if (reportList.isNotEmpty()) {
                                    showList(true)
                                    reportAdapter = ReportListAdapter(
                                        activity,
                                        reportList,
                                        onItemSelected = { reportData ->
                                            val intent =
                                                Intent(
                                                    activity,
                                                    ReportOrderDetailsActivity::class.java
                                                )
                                            intent.putExtra("data", Gson().toJson(reportData))
//                                            startActivity(intent)
                                            myActivityResultLauncher.launch(intent)
                                        },
                                        onChangeDeliveryStatus = { reportData ->
                                            AppAlertDialog(activity)
                                                .setTitleText(getString(R.string.dialog_confirm))
                                                .setContentText(
                                                    getString(
                                                        R.string.alert_delivered_order,
                                                        reportData.userName
                                                    )
                                                )
                                                .setConfirmText(getString(R.string.dialog_yes))
                                                .setDialogCancelable(true)
                                                .setConfirmClickListener { appAlertDialog ->
                                                    appAlertDialog.dismissWithAnimation()
                                                    changeDeliveryStatus(reportData)
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
                                    binding.rvReport.adapter = reportAdapter

                                } else {
                                    showList(false, getString(R.string.error_no_report))
                                }

                            } else {
                                showList(false, reportResponse.message)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
                        showList(false, t.message)
                    }
                })

        } else {
            showList(false, getString(R.string.error_internet_msg))
        }
    }

    private fun generateOrder() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            RetroClient.apiService.generateOrders(AppData.generateOrderURL)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        // if response is not successful
                        if (!response.isSuccessful) {
                            parentActivity.showDialog(
                                activity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val generateOrderResponse = response.body()
                        if (generateOrderResponse != null) {

                            if (!generateOrderResponse.succeeded) {
                                parentActivity.showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = generateOrderResponse.message
                                )
                            }
                        }

                        loadReport()
                    }

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        parentActivity.dismissLoader()
                        parentActivity.showDialog(
                            activity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
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

    private fun changeDeliveryStatus(reportData: ReportData) {

        if (reportData?.orderId == null) {
            parentActivity.showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                msg = getString(R.string.error_went_wrong)
            )
            return
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            val params = ChangeOrderStatusRequest(orderStatus = OrderStatus.DELIVERED.value)

            RetroClient.apiService.changeOrderStatus(
                "${AppData.updateOrderStatusURL}/${reportData.orderId}",
                params
            )
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

                                        updateStatus(
                                            deliveredOrderResponse.data,
                                            OrderStatus.DELIVERED.value
                                        )
                                    }
                                    .show()
                            } else {
                                parentActivity.showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = deliveredOrderResponse.message
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
        val itemToUpdate = reportList.find { it.orderId == orderId }

        // Update the status if the order from the list is matched
        itemToUpdate?.let {
            it.orderStatus = orderStatus

            val updatedItemPosition = reportList.indexOf(it)
            reportAdapter.notifyItemChanged(updatedItemPosition)
        }
    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            rvReport.visibility = if (isShowList) View.VISIBLE else View.GONE
            btnTotalFlower.visibility = if (isShowList) View.VISIBLE else View.GONE
            ivDownloadReport.visibility = if (isShowList) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowList) View.GONE else View.VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    private fun saveToCSVFile() {
        val initialDateStr = binding.tvSelectedDate.text.toString()

        // Copy values from one list to another using the extension function
//        reportListToSend = reportList.map { it.toReportDataToSend() }
        val reportListToSend = reportList.map { it.toReportDataToSend().data }

        val jsonFormatString = Gson().toJson(reportListToSend)

        if (reportList.size > 0) {
            val jsonArray = JSONArray(jsonFormatString)

            val file =
                File(activity.filesDir.absolutePath, "/report/$initialDateStr.csv")
            val csv: String = CDL.toString(jsonArray)
            FileUtils.writeStringToFile(file, csv)

            openWhatsapp(file)
        }
    }

    private fun ReportData.toReportDataToSend(): ReportDataToSend {
        return ReportDataToSend(
            linkedMapOf(
                "Flower Name" to this.flowerName,
                "Telugu Name" to this.flowerTeluguName,
                "Quantity" to this.qty,
                "Flower Type" to if (flowerType == FlowerType.LOOSE_FLOWER.value) getString(R.string.loose_flowers) else getString(
                    R.string.mora
                ),
                "Block" to this.block,
                "Flat No" to this.flatNo,
                "Name" to this.userName,
                "Mobile Number" to this.mobileNumber,
                "Price" to this.price,
                "Total Price" to this.totalPrice,
                "Order Status" to when (orderStatus) {
                    OrderStatus.DELIVERED.value -> getString(R.string.status_delivered)
                    OrderStatus.PENDING.value -> getString(R.string.status_pending)
                    OrderStatus.CANCELED.value -> getString(R.string.status_cancelled)
                    else -> getString(R.string.status_pending)
                }
            )
        )
    }

    /*private fun ReportData.toReportDataToSend(): ReportDataToSend {
        return ReportDataToSend(
            flowerName = this.flowerName,
            flowerTeluguName = this.flowerTeluguName,
            qty = this.qty,
            userName = this.userName,
            price = this.price,
            totalPrice = this.totalPrice,
            flowerType = if (flowerType == FlowerType.LOOSE_FLOWER.value) getString(R.string.loose_flowers) else getString(
                R.string.mora
            ),
            orderStatus = when (orderStatus) {
                OrderStatus.DELIVERED.value -> getString(R.string.status_delivered)
                OrderStatus.PENDING.value -> getString(R.string.status_pending)
                OrderStatus.CANCELED.value -> getString(R.string.status_cancelled)
                else -> getString(R.string.status_pending)
            }
        )
    }*/

    private fun openWhatsapp(file: File) {
        val initialDateStr = binding.tvSelectedDate.text.toString()

        val userDetails = AppPreference(activity).getUserDetails()

        val phoneNumber = "91${userDetails?.mobileNumber}"
        val fileUri = FileProvider.getUriForFile(activity, "${activity.packageName}.provider", file)

        val intent = Intent("android.intent.action.SEND")
        intent.putExtra("jid", "$phoneNumber@s.whatsapp.net")
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        intent.type = "application/csv" // Set the appropriate MIME type based on your file type

        // Optionally, you can add a text message
        intent.putExtra(Intent.EXTRA_TEXT, "$initialDateStr Report")

        intent.setPackage("com.whatsapp")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed on the device
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val initialDateStr = binding.tvSelectedDate.text.toString()

        val initialDate = dateFormat.parse(initialDateStr)
        calendar.time = initialDate

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(activity, { _, year, month, day ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)

//            selectedDate = selectedCalendar.time.toString()
            val selectedDate = dateFormat.format(selectedCalendar.time)
            binding.tvSelectedDate.text = selectedDate

            loadReport()

        }, year, month, day)

        // Set the minimum date to today
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    private fun getFormattedCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    private var myActivityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val intentData = result.data
            when (result.resultCode) {

                REQ_VIEW_ORDER -> if (intentData != null) {
                    val updatedFlower = Gson().fromJson(
                        intentData.getStringExtra("updatedOrder"),
                        ReportData::class.java
                    )
                    updateStatus(updatedFlower.orderId, updatedFlower.orderStatus)
                }
            }
        }

    private var baseDocumentTreeUri: Uri? = null
    private fun launchBaseDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        launcher.launch(intent)
    }

    private var launcher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val intentData = result.data

            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    if (intentData != null) {
                        baseDocumentTreeUri = intentData.data
                        val takeFlags =
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                        // take persistable Uri Permission for future use
                        activity.contentResolver.takePersistableUriPermission(
                            baseDocumentTreeUri!!,
                            takeFlags
                        )

                        AppPreference(activity).setPreference(
                            AppPersistence.keys.STORAGE_PATH,
                            baseDocumentTreeUri.toString()
                        )
                    }
                }
            }
        }
}