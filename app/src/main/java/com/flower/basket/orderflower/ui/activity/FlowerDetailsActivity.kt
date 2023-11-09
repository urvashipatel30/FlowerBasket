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
import com.flower.basket.orderflower.ui.adapter.DaysAdapter
import com.flower.basket.orderflower.data.Day
import com.flower.basket.orderflower.data.FlowerData
import com.flower.basket.orderflower.databinding.ActivityFlowerDetailsBinding
import com.flower.basket.orderflower.utils.BuyOption
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FlowerDetailsActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityFlowerDetailsBinding

    private var buyOption = 0
    private var flowerData: FlowerData? = null

    private var productType = 0
    private val gramsQty = 100
    private val moraQty = 1

    private val dateFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.US)

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
                buyOption = intent.getIntExtra("buyOption", BuyOption.Subscribe.value)

            if (intent.hasExtra("data"))
                flowerData = Gson().fromJson(intent.getStringExtra("data"), FlowerData::class.java)
        }

        binding.apply {
            ivFlowerName.text = flowerData?.name

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
        binding.tvQtyMinus.setOnClickListener(this)
        binding.tvQtyPlus.setOnClickListener(this)
        binding.endDateLayout.setOnClickListener(this)
        binding.ivRemoveEndDate.setOnClickListener(this)

        when (buyOption) {
            0 -> {
                binding.cardDaySelection.visibility = View.VISIBLE
                binding.tvStartLabel.text = getString(R.string.starts_on)
                binding.tvEndsLabel.text = getString(R.string.ends_on)
                binding.tvEndDate.text = ""

                binding.btnOrder.text = getString(R.string.subscribe)
            }

            1 -> {
                binding.cardDaySelection.visibility = View.GONE
                binding.tvStartLabel.text = getString(R.string.orders_on)
                binding.tvEndsLabel.text = getString(R.string.delivers_on)
                binding.tvEndDate.text = getFormattedTomorrowDate()

                binding.btnOrder.text = getString(R.string.order)
            }
        }


        // Create & Set the ArrayAdapter for the Spinner
        val productTypes = arrayOf("Loose Flowers", "Mora")
        val spnAdapter = ArrayAdapter(this, R.layout.spinner_item_day, productTypes)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProductType.adapter = spnAdapter

        // Set an OnItemSelectedListener for the Spinner
        binding.spinnerProductType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    productType = position
                    // Handle the selected item (e.g., update the price based on the selected item)
                    binding.tvMeasurement.text =
                        if (position == 0) getString(R.string.grams) else getString(R.string.mora)
                    setDefaultQuantity(position)
                    calculatePrice()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        binding.rvWeekDays.apply {
            layoutManager = LinearLayoutManager(activity)

            adapter = DaysAdapter(daysList, object : DaysAdapter.DaySelectionCallback {
                override fun onDaysSelected(selectedDays: String) {
                    Log.e("onDaysSelected: ", "Selected days: $selectedDays")
                }
            })
        }
    }

    private fun setDefaultQuantity(selectedPosition: Int) {
        when (selectedPosition) {
            0 -> binding.tvQuantity.text = "$gramsQty"
            1 -> binding.tvQuantity.text = "$moraQty"
            else -> binding.tvQuantity.text = ""
        }
    }

    private fun calculatePrice() {
        val selectedProduct = binding.spinnerProductType.selectedItem.toString()
        val quantity = binding.tvQuantity.text.toString()

        // Implement price calculation logic here based on the selected product and quantity
        // For simplicity, we'll use a fixed price for each product
        val price = when (selectedProduct) {
            "Loose Flowers" -> 5.0
            "Mora" -> 10.0
            else -> 0.0
        }

        if (quantity.isNotEmpty()) {
            val totalPrice = price * quantity.toDouble()
            binding.tvPrice.text = "₹$totalPrice"
        } else {
            binding.tvPrice.text = "N/A"
        }
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.backLayout.ivBackAction -> onBackPressedDispatcher.onBackPressed()

            binding.tvQtyMinus -> {
                var qty: Int = binding.tvQuantity.text.toString().toInt()
                val qtyToMinus = if (productType == 0) gramsQty else moraQty
                if (qty > qtyToMinus) qty -= qtyToMinus
                binding.tvQuantity.text = "$qty"
                calculatePrice()
            }

            binding.tvQtyPlus -> {
                var qty: Int = binding.tvQuantity.text.toString().toInt()
                val qtyToPlus = if (productType == 0) gramsQty else moraQty
                qty += qtyToPlus
                binding.tvQuantity.text = "$qty"
                calculatePrice()
            }

            binding.endDateLayout -> showDatePicker()

            binding.ivRemoveEndDate -> {
                binding.tvEndDate.text = ""
                binding.ivRemoveEndDate.visibility = View.GONE
            }
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

            val formattedDate = dateFormat.format(selectedCalendar.time)
            binding.tvEndDate.text = formattedDate

            Log.e("showDatePicker: ", "endDate => ${binding.tvEndDate.text}")

            binding.ivRemoveEndDate.visibility = if (buyOption == 0) View.VISIBLE else View.GONE

        }, year, month, day)

        // Set the minimum date to tomorrow
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)
        datePickerDialog.datePicker.minDate = tomorrow.timeInMillis

        datePickerDialog.show()
    }
}