package com.flower.basket.orderflower.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.Day
import com.flower.basket.orderflower.data.SubscriptionItemData
import com.flower.basket.orderflower.data.SubscriptionItemResponse
import com.flower.basket.orderflower.data.UpdateSubscriptionRequest
import com.flower.basket.orderflower.data.UpdateSubscriptionResponse
import com.flower.basket.orderflower.databinding.ActivityFlowerDetailsBinding
import com.flower.basket.orderflower.ui.adapter.DaysAdapter
import com.flower.basket.orderflower.ui.fragment.SubscriptionsFragment
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.Quantity
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditSubscriptionActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityFlowerDetailsBinding

    private var subscriptionData: SubscriptionItemData? = null
    private var subscriptionID: String? = null

    private var flowerType = FlowerType.LOOSE_FLOWER.value
    private var quantityToOrder = 1
    private val gramsQty = Quantity.GRAMS.value
    private val moraQty = Quantity.MORA.value

    private val looseFlower = FlowerType.LOOSE_FLOWER.value
    private val mora = FlowerType.MORA.value

    private var quantity: Int = 1
    private var flowerPrice: Int? = 0
    private var totalPrice: Double? = 0.00

    private var selectedDaysInterval = ""
    private var subscriptionEndDate: String? = null
    private val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)

    var isPlaceOrderClickable = true

    private var errorMsg: String? = null

    private val daysList = mutableListOf(
        Day("All", -1, false), // -1 represents the "All" option
        Day("Sunday", 0, false),
        Day("Monday", 1, false),
        Day("Tuesday", 2, false),
        Day("Wednesday", 3, false),
        Day("Thursday", 4, false),
        Day("Friday", 5, false),
        Day("Saturday", 6, false)
    )
    private var daysAdapter: DaysAdapter? = null

    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@EditSubscriptionActivity

        if (intent != null) {
            if (intent.hasExtra("subscriptionID"))
                subscriptionID = intent.getStringExtra("subscriptionID")

            if (intent.hasExtra("data"))
                subscriptionData =
                    Gson().fromJson(intent.getStringExtra("data"), SubscriptionItemData::class.java)
        }

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.btnOrder.setOnClickListener(this)
        binding.tvQtyMinus.setOnClickListener(this)
        binding.tvQtyPlus.setOnClickListener(this)
        binding.endDateLayout.setOnClickListener(this)
        binding.ivRemoveEndDate.setOnClickListener(this)

        selectedDaysInterval = subscriptionData?.interval.toString()

        // Create & Set the ArrayAdapter for the Spinner
        val productTypes = arrayOf(getString(R.string.loose_flowers), getString(R.string.mora))
        val spnAdapter = ArrayAdapter(this, R.layout.spinner_item_day, productTypes)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.apply {
            tvSelectedDaysLabel.text = getString(R.string.selected_days)
            cardDaySelection.visibility = View.VISIBLE
            cardStartEndDate.visibility = View.GONE
            tvStartLabel.text = getString(R.string.starts_on)
            tvEndsLabel.text = getString(R.string.ends_on)
            tvEndDate.text = ""
            btnOrder.text = getString(R.string.subscribe)

            spinnerProductType.adapter = spnAdapter
            // Set an OnItemSelectedListener for the Spinner
            spinnerProductType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        flowerType = position
                        Log.e("onItemSelected: ", "position => $position")
                        // Handle the selected item (e.g., update the price based on the selected item)
                        binding.tvMeasurement.text =
                            if (position == 0) getString(R.string.grams) else getString(R.string.mora)

                        setDefaultQuantity(position)
                        setFlowerPrice(position)
                        calculatePrice()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }

        daysAdapter =
            DaysAdapter(daysList, isFromEdit = true, object : DaysAdapter.DaySelectionCallback {
                override fun onDaysSelected(selectedDays: String) {
                    Log.e("onDaysSelected: ", "Selected days: $selectedDays")
                    selectedDaysInterval = selectedDays
                }
            })
        binding.rvWeekDays.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = daysAdapter
        }

        getSubscriptionDetail()
    }

    private fun setDetails() {
        quantity = subscriptionData?.qty!!

        binding.apply {
            tvFlowerName.text =
                if (subscriptionData?.flowerTeluguName?.isNotEmpty() == true) subscriptionData?.flowerTeluguName else subscriptionData?.flowerName

            ivRemoveEndDate.visibility = View.GONE
            tvQuantity.text = "$quantity"
        }

        daysAdapter?.selectDaysByString(subscriptionData?.interval.toString())

        Glide.with(binding.ivFlowerImage.context)
            .load(subscriptionData?.flowerImageUrl)
            .placeholder(R.drawable.ic_profile_holder)
            .error(R.drawable.ic_profile_holder)
            .centerCrop()
            .into(binding.ivFlowerImage)

        subscriptionData?.flowerType?.let { binding.spinnerProductType.setSelection(it) }
        binding.spinnerProductType.visibility = View.INVISIBLE
    }

    private fun getSubscriptionDetail() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            showLoader(activity)

            RetroClient.apiService.getSubscriptionsDetail(subscriptionData?.id.toString())
                .enqueue(object : Callback<SubscriptionItemResponse> {
                    override fun onResponse(
                        call: Call<SubscriptionItemResponse>,
                        response: Response<SubscriptionItemResponse>
                    ) {
                        dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            errorMsg = response.message()
                            showDetail(false, errorMsg)
                            return
                        }

                        val itemResponse = response.body()
                        Log.e("onResponse: ", "Subscription itemResponse => $itemResponse")

                        if (itemResponse != null) {
                            if (itemResponse.succeeded) {
                                // Handle the retrieved subscription data
                                subscriptionData = itemResponse.data
                                Log.e("onResponse: ", "subscriptionData => ${subscriptionData}")

                                if (subscriptionData != null) {
                                    showDetail(true)
                                    setDetails()

                                } else showDetail(false, errorMsg)

                            } else {
                                errorMsg = itemResponse.message
                                showDetail(false, errorMsg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SubscriptionItemResponse>, t: Throwable) {
                        dismissLoader()
                        errorMsg = t.message
                        showDetail(false, errorMsg)
                    }
                })
        } else {
            errorMsg = getString(R.string.error_internet_msg)
            showDetail(false, errorMsg)
        }
    }

    private fun showDetail(
        isShowDetail: Boolean = true,
        msg: String? = null
    ) {
        binding.apply {
            contentView.visibility = if (isShowDetail) View.VISIBLE else View.GONE
            llDataErrorView.visibility = if (isShowDetail) View.GONE else View.VISIBLE

            if (!isShowDetail) {
                tvDataError.text = msg ?: getString(R.string.error_went_wrong)
            }
        }
    }

    private fun setDefaultQuantity(flowerTypePosition: Int) {
        binding.tvQuantity.text = quantityToOrder.toString()
        binding.tvQuantity.text = subscriptionData?.qty?.toString()

        binding.tvTotalQty.text = when (flowerTypePosition) {
            looseFlower -> (subscriptionData?.qty?.times(gramsQty)).toString()
            mora -> (subscriptionData?.qty?.times(moraQty)).toString()
            else -> (subscriptionData?.qty?.times(gramsQty)).toString()
        }

//        binding.tvQuantity.text = when (flowerTypePosition) {
//            looseFlower -> (subscriptionData?.qty?.times(gramsQty)).toString()
//            mora -> (subscriptionData?.qty?.times(moraQty)).toString()
//            else -> (subscriptionData?.qty?.times(gramsQty)).toString()
//        }

        Log.e("setDefaultQuantity: ", "Final Qty => ${binding.tvQuantity.text.toString()}")
    }

    private fun setFlowerPrice(flowerTypePosition: Int) {
        flowerPrice = when (flowerTypePosition) {
            looseFlower -> subscriptionData?.loosePrice
            mora -> subscriptionData?.moraPrice
            else -> subscriptionData?.loosePrice
        }
        Log.e("onCreate: ", "subscriptionData => $subscriptionData")
        binding.tvPrice.text = getString(R.string.rupee_symbol, flowerPrice?.toDouble())
    }

    private fun calculatePrice() {
        val selectedProduct = binding.spinnerProductType.selectedItem.toString()
        var quantity = binding.tvQuantity.text.toString()
//        if (flowerType == looseFlower) quantity = (quantity.toInt() / 100).toString()

        // Implement price calculation logic based on the selected flower and quantity
        if (quantity.isNotEmpty()) {
            totalPrice = flowerPrice?.times(quantity.toDouble())
            binding.tvPrice.text = getString(R.string.rupee_symbol, totalPrice)
//            binding.tvPrice.text = totalPrice.toString()
        } else {
            binding.tvPrice.text = "N/A"
        }
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.backLayout.ivBackAction -> onBackPressedDispatcher.onBackPressed()

            binding.btnOrder -> {
                if (selectedDaysInterval.isEmpty()) {
                    showDialog(activity, msg = getString(R.string.error_select_interval))
                } else {
                    if (isPlaceOrderClickable) updateSubscription()
                }
            }

            binding.tvQtyMinus -> {
                var qty: Int = binding.tvQuantity.text.toString().toInt()
                val qtyToMinus = /*if (flowerType == looseFlower) gramsQty else*/ moraQty
                if (qty > qtyToMinus) qty -= qtyToMinus
                binding.tvQuantity.text = "$qty"
                binding.tvTotalQty.text = when (flowerType) {
                    looseFlower -> (qty.times(gramsQty)).toString()
                    mora -> (qty.times(moraQty)).toString()
                    else -> (qty.times(gramsQty)).toString()
                }
                calculatePrice()
            }

            binding.tvQtyPlus -> {
                var qty: Int = binding.tvQuantity.text.toString().toInt()
                val qtyToPlus = /*if (flowerType == looseFlower) gramsQty else*/ moraQty
                qty += qtyToPlus
                binding.tvQuantity.text = "$qty"
                binding.tvTotalQty.text = when (flowerType) {
                    looseFlower -> (qty.times(gramsQty)).toString()
                    mora -> (qty.times(moraQty)).toString()
                    else -> (qty.times(gramsQty)).toString()
                }
                calculatePrice()
            }

            binding.endDateLayout -> showDatePicker()

            binding.ivRemoveEndDate -> {
                binding.tvEndDate.text = ""
                binding.ivRemoveEndDate.visibility = View.GONE
            }
        }
    }

    private fun updateSubscription() {
        val qtyText = binding.tvQuantity.text.toString().toInt()
//        quantityToOrder = if (flowerType == looseFlower) qtyText / gramsQty else qtyText / moraQty
        quantityToOrder = qtyText
        Log.e("updateSubscription: ", "quantityToOrder => $quantityToOrder")

        if (NetworkUtils.isNetworkAvailable(activity)) {
            binding.btnOrder.isEnabled = false
            isPlaceOrderClickable = false

            showLoader(activity)

            if (subscriptionData?.id == null) {
                dismissLoader()
                binding.btnOrder.isEnabled = true
                isPlaceOrderClickable = true

                showDialog(
                    activity,
                    dialogType = AppAlertDialog.ERROR_TYPE,
                    msg = getString(R.string.error_went_wrong)
                )
                return
            }

            val params = UpdateSubscriptionRequest(
                qty = quantityToOrder,
                interval = selectedDaysInterval
            )
            Log.e("placeOrder: ", "updateSubscriptions Data => $params")

            RetroClient.apiService.updateSubscription(subscriptionData?.id!!, params)
                .enqueue(object : Callback<UpdateSubscriptionResponse> {
                    override fun onResponse(
                        call: Call<UpdateSubscriptionResponse>,
                        response: Response<UpdateSubscriptionResponse>
                    ) {
                        binding.btnOrder.isEnabled = true
                        isPlaceOrderClickable = true

                        dismissLoader()
                        Log.e(
                            "updateSubscriptions: ",
                            "response => $response, ${response.isSuccessful}"
                        )

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showDialog(
                                activity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val subscriptionResponse = response.body()
                        if (subscriptionResponse != null) {
                            if (subscriptionResponse.succeeded) {
                                // Handle the retrieved user data
                                val subscriptionID = subscriptionResponse.data
                                Log.e("updateSubscriptions: ", "subscriptionID => $subscriptionID")

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success_updated))
                                    .setContentText(subscriptionResponse.message)
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()

                                        setResult(
                                            SubscriptionsFragment.REQ_EDIT_SUBSCRIPTION,
                                            Intent().putExtra("updatedQty", quantityToOrder)
                                        )
                                        finish()
                                    }
                                    .show()
                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = subscriptionResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<UpdateSubscriptionResponse>, t: Throwable) {
                        dismissLoader()
                        binding.btnOrder.isEnabled = true
                        isPlaceOrderClickable = true

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

    private fun getFormattedCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)
        return dateFormat.format(calendar.time)
    }

    private fun getFormattedTomorrowDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Add one day to get tomorrow's date

        val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)
        return dateFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val initialDateStr = binding.tvEndDate.text.toString()

        if (initialDateStr != "") {
            val initialDate = dateFormat.parse(initialDateStr)

            if (initialDate != null)
                calendar.time = initialDate
            else
                calendar.add(Calendar.DAY_OF_YEAR, 1)

        } else {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)

            subscriptionEndDate = selectedCalendar.time.toString()
            val formattedDate = dateFormat.format(selectedCalendar.time)
            binding.tvEndDate.text = formattedDate

            Log.e("showDatePicker: ", "endDate => ${binding.tvEndDate.text}")
            binding.ivRemoveEndDate.visibility = View.VISIBLE

        }, year, month, day)

        // Set the minimum date to tomorrow
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)
        datePickerDialog.datePicker.minDate = tomorrow.timeInMillis

        datePickerDialog.show()
    }
}