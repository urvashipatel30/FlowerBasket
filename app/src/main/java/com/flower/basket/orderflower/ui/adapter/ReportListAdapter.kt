package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.report.ReportData
import com.flower.basket.orderflower.databinding.ItemReportBinding
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Quantity

class ReportListAdapter(
    var activity: Activity,
    private val reportList: ArrayList<ReportData>,
    private val onItemSelected: (ReportData) -> Unit,
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
        fun bind(report: ReportData) {

            binding.tvFlowerName.text = report.flowerName
            binding.tvFlowerName.requestFocus()

            if (report.flowerTeluguName.isNotEmpty()) binding.tvFlowerTeluguName.text =
                report.flowerTeluguName
            else binding.tvFlowerTeluguName.visibility = View.GONE

            binding.tvAddress.text = "${report.block} - ${report.flatNo} "

            Glide.with(binding.ivFlowerPhoto.context)
                .load(report.flowerImageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)

            val measurement =
                if (report.flowerType == FlowerType.LOOSE_FLOWER.value) activity.getString(R.string.grams)
                else activity.getString(R.string.mora)
            val qty =
                if (report.flowerType == FlowerType.LOOSE_FLOWER.value) ((report.qty) * Quantity.GRAMS.value)
                else ((report.qty) * Quantity.MORA.value)
            binding.tvQuantity.text = "$qty $measurement"

            binding.tvOrderStatus.text = when (report.orderStatus) {
                OrderStatus.PENDING.value -> activity.getString(R.string.status_pending)
                OrderStatus.CANCELED.value -> activity.getString(R.string.status_cancelled)
                OrderStatus.DELIVERED.value -> activity.getString(R.string.status_delivered)
                else -> activity.getString(R.string.status_unknown)
            }

            binding.tvTotalPrice.text =
                activity.getString(R.string.rupee_symbol, report.totalPrice.toDouble())

            binding.llDeliveredOrder.visibility =
                if (report.orderStatus == OrderStatus.PENDING.value) View.VISIBLE else View.GONE

            val foregroundDrawable = if (report.orderStatus == OrderStatus.DELIVERED.value) {
                ColorDrawable(ContextCompat.getColor(activity, R.color.cancelled_orderColor))
            } else {
                ColorDrawable(ContextCompat.getColor(activity, R.color.transparent))
            }
            val container = binding.orderDetailView.parent as? FrameLayout
            container?.foreground = foregroundDrawable

            binding.cardItem.setOnClickListener {
                onItemSelected.invoke(report)
            }

            binding.btnDeliveredOrder.setOnClickListener {
                onChangeDeliveryStatus.invoke(report)
            }
        }
    }
}