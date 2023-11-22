package com.flower.basket.orderflower.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
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
import com.flower.basket.orderflower.data.FlowerData
import com.flower.basket.orderflower.data.OrderRequest
import com.flower.basket.orderflower.data.PlaceOrderResponse
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.databinding.ActivityFlowerDetailsBinding
import com.flower.basket.orderflower.ui.adapter.DaysAdapter
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.utils.Quantity
import com.flower.basket.orderflower.utils.SubscriptionType
import com.flower.basket.orderflower.utils.Utils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FlowerDetailsActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityFlowerDetailsBinding

    private var buyOption = SubscriptionType.Subscribe.value
    private var flowerData: FlowerData? = null

    private var flowerType = FlowerType.LOOSE_FLOWER.value
    private var quantityToOrder = 1
    private val gramsQty = Quantity.GRAMS.value
    private val moraQty = Quantity.MORA.value

    private val looseFlower = FlowerType.LOOSE_FLOWER.value
    private val mora = FlowerType.MORA.value

    private var flowerPrice: Int? = 0
    private var totalPrice: Double? = 0.00

    private var selectedDaysInterval = ""
    private var subscriptionEndDate: String? = null
    private val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)

    var isPlaceOrderClickable = true

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

    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@FlowerDetailsActivity

        if (intent != null) {
            if (intent.hasExtra("buyOption"))
                buyOption = intent.getIntExtra("buyOption", SubscriptionType.Subscribe.value)

            if (intent.hasExtra("data"))
                flowerData = Gson().fromJson(intent.getStringExtra("data"), FlowerData::class.java)
        }

        binding.apply {
            tvFlowerName.text =
                if (flowerData?.teluguName?.isNotEmpty() == true) flowerData?.teluguName else flowerData?.name

            tvQuantity.text = quantityToOrder.toString()
            ivRemoveEndDate.visibility = View.GONE
            tvStartDate.text = getFormattedCurrentDate()
        }

        Glide.with(binding.ivFlowerImage.context)
            .load(flowerData?.imageUrl)
            .placeholder(R.drawable.ic_profile_holder)
            .error(R.drawable.ic_profile_holder)
            .centerCrop()
            .into(binding.ivFlowerImage)

        binding.backLayout.ivBackAction.setOnClickListener(this)
        binding.btnOrder.setOnClickListener(this)
        binding.tvQtyMinus.setOnClickListener(this)
        binding.tvQtyPlus.setOnClickListener(this)
        binding.endDateLayout.setOnClickListener(this)
        binding.ivRemoveEndDate.setOnClickListener(this)

        when (buyOption) {
            SubscriptionType.BuyOnce.value -> {
                binding.cardDaySelection.visibility = View.GONE
                binding.cardStartEndDate.visibility = View.VISIBLE

                binding.tvStartLabel.text = getString(R.string.orders_on)
                binding.tvEndsLabel.text = getString(R.string.delivers_on)
                binding.tvEndDate.text = getFormattedTomorrowDate()

                binding.btnOrder.text = getString(R.string.order)
            }

            SubscriptionType.Subscribe.value -> {
                binding.cardDaySelection.visibility = View.VISIBLE
                binding.cardStartEndDate.visibility = View.GONE

                binding.tvStartLabel.text = getString(R.string.starts_on)
                binding.tvEndsLabel.text = getString(R.string.ends_on)
                binding.tvEndDate.text = ""

                binding.btnOrder.text = getString(R.string.subscribe)
            }
        }


        // Create & Set the ArrayAdapter for the Spinner
        val productTypes = arrayOf(getString(R.string.loose_flowers), getString(R.string.mora))
        val spnAdapter = ArrayAdapter(this, R.layout.spinner_item_day, productTypes)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProductType.adapter = spnAdapter

        binding.spinnerProductType.setSelection(0)

        // Set an OnItemSelectedListener for the Spinner
        binding.spinnerProductType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    flowerType = position
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


        binding.rvWeekDays.apply {
            layoutManager = LinearLayoutManager(activity)

            adapter = DaysAdapter(
                daysList,
                isFromEdit = false,
                object : DaysAdapter.DaySelectionCallback {
                    override fun onDaysSelected(selectedDays: String) {
                        Log.e("onDaysSelected: ", "Selected days: $selectedDays")
                        selectedDaysInterval = selectedDays
                    }
                })
        }
    }

    private fun setFlowerPrice(flowerTypePosition: Int) {
        flowerPrice = when (flowerTypePosition) {
            looseFlower -> flowerData?.loosePrice
            mora -> flowerData?.moraPrice
            else -> flowerData?.loosePrice
        }
        binding.tvPrice.text = getString(R.string.rupee_symbol, flowerPrice?.toDouble())
    }

    private fun setDefaultQuantity(flowerTypePosition: Int) {
        binding.tvQuantity.text = quantityToOrder.toString()
        val quantity = binding.tvQuantity.text.toString()

        binding.tvTotalQty.text = when (flowerTypePosition) {
            looseFlower -> (quantity.toInt().times(gramsQty)).toString()
            mora -> (quantity.toInt().times(moraQty)).toString()
            else -> (quantity.toInt().times(gramsQty)).toString()
        }

//        binding.tvQuantity.text = when (flowerTypePosition) {
//            looseFlower -> "$gramsQty"
//            mora -> "$moraQty"
//            else -> "$gramsQty"
//        }
    }

    private fun calculatePrice() {
        val selectedProduct = binding.spinnerProductType.selectedItem.toString()
        var quantity = binding.tvQuantity.text.toString()
//        if (flowerType == looseFlower) quantity = (quantity.toInt() / 100).toString()

        // Implement price calculation logic based on the selected product and quantity
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
                if (buyOption == SubscriptionType.Subscribe.value && selectedDaysInterval.isEmpty()) {
                    showDialog(activity, msg = getString(R.string.error_select_interval))
                } else {
                    if (isPlaceOrderClickable) placeOrder()
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

    private fun placeOrder() {
        val userDetails = AppPreference(activity).getUserDetails()

        val qtyText = binding.tvQuantity.text.toString().toInt()
//        quantityToOrder = if (flowerType == looseFlower) qtyText / gramsQty else qtyText / moraQty
        quantityToOrder = qtyText
        Log.e("placeOrder: ", "quantityToOrder => $quantityToOrder")

        if (subscriptionEndDate != null) {
            subscriptionEndDate = Utils().convertDateToISO8601(subscriptionEndDate!!)
        }
        Log.e("placeOrder: ", "subscriptionEndDate => $subscriptionEndDate")

        if (NetworkUtils.isNetworkAvailable(activity)) {
            binding.btnOrder.isEnabled = false
            isPlaceOrderClickable = false

            showLoader(activity)

            if (userDetails?.id == null || flowerData?.id == null) {
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

            val params = OrderRequest(
                userId = userDetails.id,
                flowerId = flowerData?.id!!,
                subscriptionType = buyOption,
                qty = quantityToOrder,
                flowerType = flowerType,
                interval = selectedDaysInterval,
                subscriptionEndDate = subscriptionEndDate
            )
            Log.e("placeOrder: ", "OrderData => $params")

            RetroClient.apiService.placeOrder(params)
                .enqueue(object : Callback<PlaceOrderResponse> {
                    override fun onResponse(
                        call: Call<PlaceOrderResponse>,
                        response: Response<PlaceOrderResponse>
                    ) {
                        binding.btnOrder.isEnabled = true
                        isPlaceOrderClickable = true

                        dismissLoader()
                        Log.e("placeOrder: ", "response => $response, ${response.isSuccessful}")

                        // if response is not successful
                        if (!response.isSuccessful) {
                            showDialog(
                                activity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val orderResponse = response.body()
                        Log.e("placeOrder: ", "orderResponse => $orderResponse")
                        Log.e("placeOrder: ", "succeeded => ${orderResponse?.succeeded}")

                        if (orderResponse != null) {
                            if (orderResponse.succeeded) {
                                // Handle the retrieved user data
                                val orderId = orderResponse.data
                                Log.e("onResponse: ", "orderId => $orderId")

                                AppAlertDialog(activity, AppAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(
                                        if (buyOption == SubscriptionType.BuyOnce.value) getString(
                                            R.string.order_placed_success
                                        ) else getString(R.string.subscription_started_success)
                                    )
                                    .setConfirmText(getString(R.string.dialog_ok))
                                    .setConfirmClickListener { appAlertDialog ->
                                        appAlertDialog.dismissWithAnimation()
                                        finish()
                                    }
                                    .show()
                            } else {
                                showDialog(
                                    activity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = orderResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<PlaceOrderResponse>, t: Throwable) {
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

            binding.ivRemoveEndDate.visibility =
                if (buyOption == SubscriptionType.Subscribe.value) View.VISIBLE else View.GONE

        }, year, month, day)

        // Set the minimum date to tomorrow
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)
        datePickerDialog.datePicker.minDate = tomorrow.timeInMillis

        datePickerDialog.show()
    }
}