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
import com.flower.basket.orderflower.data.order.OrderData
import com.flower.basket.orderflower.databinding.ItemOrdersBinding
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.OrderStatus
import com.flower.basket.orderflower.utils.Quantity
import com.flower.basket.orderflower.utils.Utils

class OrdersListAdapter(
    var activity: Activity,
    private val ordersList: ArrayList<OrderData>,
    private val onItemSelected: (OrderData) -> Unit,
    private val onOrderCancelled: (OrderData) -> Unit
) :
    RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemOrdersBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = ordersList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class ViewHolder(val binding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderData) {

            binding.tvFlowerName.text = order.flowerName
            if (order.flowerTeluguName.isNotEmpty()) binding.tvFlowerTeluguName.text =
                order.flowerTeluguName
            else binding.tvFlowerTeluguName.visibility = View.GONE

            Glide.with(binding.ivFlowerPhoto.context)
                .load(order.flowerImageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)

            val measurement =
                if (order.flowerType == FlowerType.LOOSE_FLOWER.value) activity.getString(R.string.grams)
                else activity.getString(R.string.mora)

            val qty =
                if (order.flowerType == FlowerType.LOOSE_FLOWER.value) ((order.qty) * Quantity.GRAMS.value)
                else ((order.qty) * Quantity.MORA.value)
            binding.tvQuantity.text = "$qty $measurement"

            binding.tvOrderStatus.text = when (order.orderStatus) {
                OrderStatus.PENDING.value -> activity.getString(R.string.status_pending)
                OrderStatus.CANCELED.value -> activity.getString(R.string.status_cancelled)
                OrderStatus.DELIVERED.value -> activity.getString(R.string.status_delivered)
                OrderStatus.IN_DELIVERY.value -> activity.getString(R.string.status_in_delivery)
                else -> activity.getString(R.string.status_unknown)
            }

            binding.llCancelOrder.visibility =
                if (order.orderStatus == OrderStatus.PENDING.value || order.orderStatus == OrderStatus.IN_DELIVERY.value) View.VISIBLE else View.GONE

            val foregroundDrawable = if (order.orderStatus == OrderStatus.CANCELED.value) {
                ColorDrawable(ContextCompat.getColor(activity, R.color.cancelled_orderColor))
            } else {
                ColorDrawable(ContextCompat.getColor(activity, R.color.transparent))
            }
            val container = binding.orderDetailView.parent as? FrameLayout
            container?.foreground = foregroundDrawable

//            binding.orderDetailView.foreground =
//                if (order.orderStatus == OrderStatus.CANCELED.value) ColorDrawable(ContextCompat.getColor(activity, R.color.cancelled_orderColor))
//                else ColorDrawable(ContextCompat.getColor(activity, R.color.transparent))

            try {
                binding.tvDeliveryDate.text = Utils().convertFromOrderDateFormat(order.deliveryDate)
            } catch (e: Exception) {
                binding.tvDeliveryDate.visibility = View.GONE
                e.printStackTrace()
            }

            binding.tvTotalPrice.text =
                activity.getString(R.string.rupee_symbol, order.totalPrice.toDouble())

            binding.cardItem.setOnClickListener {
                onItemSelected.invoke(order)
            }

            binding.llCancelOrder.setOnClickListener {
                onOrderCancelled.invoke(order)
            }
        }
    }
}