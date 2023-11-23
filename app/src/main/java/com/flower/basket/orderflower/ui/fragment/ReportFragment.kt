package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.ReportData
import com.flower.basket.orderflower.data.ReportDataToSend
import com.flower.basket.orderflower.data.ReportResponse
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.FragmentReportBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.adapter.ReportListAdapter
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Utils
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
import java.util.Objects


class ReportFragment : ParentFragment(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentReportBinding

    private var reportListJSON: String? = null
    private var reportList = ArrayList<ReportData>()
    private var reportListToSend = emptyList<ReportDataToSend>()
    private lateinit var reportAdapter: ReportListAdapter

    private val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = activity as DashboardActivity
        Log.e("onCreateView: ", "Report activity => $activity")

        binding.tvSelectedDate.text = getFormattedCurrentDate()

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.llChooseDate.setOnClickListener(this)
        binding.ivDownloadReport.setOnClickListener(this)

        binding.rvReport.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        loadReport()

        return binding.root
    }

    private fun loadReport() {
        val selectedDate = binding.tvSelectedDate.text.toString()

        val dateToSend = Utils().convertToAPIDateFormat(selectedDate)
        Log.e("loadReport: ", "dateToSend => $dateToSend")

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            RetroClient.apiService.getReport(dateToSend)
                .enqueue(object : Callback<ReportResponse> {
                    override fun onResponse(
                        call: Call<ReportResponse>,
                        response: Response<ReportResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showList(false, response.message())
                            return
                        }

                        val reportResponse = response.body()
                        Log.e("onResponse: ", "reportResponse => $reportResponse")

                        if (reportResponse != null) {
                            if (reportResponse.succeeded) {
                                // Handle the retrieved report data
                                reportListJSON =
                                    reportResponse.data.toString().replace("ReportData", "")
                                Log.e("onResponse: ", "reportListJSON => $reportListJSON")

                                reportList = reportResponse.data as ArrayList<ReportData>
                                Log.e("onResponse: ", "report List => ${reportList.size}")

                                if (reportList.isNotEmpty()) {
                                    showList(true)
                                    reportAdapter = ReportListAdapter(
                                        activity,
                                        reportList,
                                        onShowMore = { reportData ->

                                        },
                                        onChangeDeliveryStatus = { reportData ->
                                            changeDeliveryStatus(reportData)
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

    private fun changeDeliveryStatus(reportData: ReportData) {

    }

    private fun showList(
        isShowList: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            rvReport.visibility = if (isShowList) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowList) View.GONE else View.VISIBLE

            if (!isShowList) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
//            binding.backLayout.ivBackAction -> onBackPressedDispatcher.onBackPressed()

            binding.llChooseDate -> showDatePicker()

            binding.ivDownloadReport -> {
//                launchBaseDirectoryPicker()
                saveToCSVFile()
            }
        }
    }

    fun ReportData.toReportDataToSend(): ReportDataToSend {
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
    }


    fun saveToCSVFile() {
        val initialDateStr = binding.tvSelectedDate.text.toString()

        // Copy values from one list to another using the extension function
        reportListToSend = reportList.map { it.toReportDataToSend() }

        val jsonFormatString = Gson().toJson(reportListToSend)
        Log.e("onClick: ", "jsonFormatString => $jsonFormatString")

        if (reportList.size > 0) {
            val jsonArray = JSONArray(jsonFormatString)

            val file =
                File(activity.filesDir.absolutePath, "/report/$initialDateStr.csv")
            val csv: String = CDL.toString(jsonArray)
            FileUtils.writeStringToFile(file, csv)

            openWhatsapp(file)
        }
    }

    private fun openWhatsapp(file: File) {
        val initialDateStr = binding.tvSelectedDate.text.toString()

        val phoneNumber = "917096870086" // Replace with the desired phone number
//        val fileUri = Uri.fromFile(file) // Replace with the actual file path
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

            Log.e("showDatePicker: ", "Selected Date => ${binding.tvSelectedDate.text}")

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
            Log.e("launcher: ", "result.resultCode => ${result.resultCode}")
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    if (intentData != null) {
                        baseDocumentTreeUri = intentData.data
                        Log.e("launcher: ", "baseDocumentTreeUri => $baseDocumentTreeUri")
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