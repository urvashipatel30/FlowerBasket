package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.ReportData
import com.flower.basket.orderflower.databinding.ItemOrdersBinding
import com.flower.basket.orderflower.databinding.ItemReportBinding
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Quantity

class ReportListAdapter(
    var activity: Activity,
    private val reportList: ArrayList<ReportData>,
    private val onShowMore: (ReportData) -> Unit,
    private val onChangeDeliveryStatus: (ReportData) -> Unit
) :
    RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemReportBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = reportList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    inner class ViewHolder(val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: ReportData) {

            binding.tvFlowerName.text = order.flowerName
            if (order.flowerTeluguName.isNotEmpty()) binding.tvFlowerTeluguName.text =
                order.flowerTeluguName
            else binding.tvFlowerTeluguName.visibility = View.GONE

//            Glide.with(binding.ivFlowerPhoto.context)
//                .load(order.flowerImageUrl)
//                .placeholder(R.drawable.ic_profile_holder)
//                .error(R.drawable.ic_profile_holder)
//                .centerCrop()
//                .into(binding.ivFlowerPhoto)

            val measurement =
                if (order.flowerType == FlowerType.LOOSE_FLOWER.value) activity.getString(R.string.grams)
                else activity.getString(R.string.mora)

            val qty =
                if (order.flowerType == FlowerType.LOOSE_FLOWER.value) ((order.qty) * Quantity.GRAMS.value)
                else ((order.qty) * Quantity.MORA.value)
            binding.tvQuantity.text = "$qty"

            val status = order.orderStatus
            binding.ivDelivered.setImageResource(
                if (status == OrderStatus.DELIVERED.value) R.drawable.ic_day_checked
                else R.drawable.ic_day_unchecked
            )

//            binding.tvTotalPrice.text =
//                activity.getString(R.string.rupee_symbol, order.totalPrice.toDouble())

            binding.ivShowMore.setOnClickListener {
                onShowMore.invoke(order)
            }

            binding.ivDelivered.setOnClickListener {
                onChangeDeliveryStatus.invoke(order)
            }
        }
    }
}