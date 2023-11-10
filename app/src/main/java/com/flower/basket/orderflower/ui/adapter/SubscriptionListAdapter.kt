package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.SubscriptionListData
import com.flower.basket.orderflower.databinding.ItemSubscriptionBinding
import com.flower.basket.orderflower.utils.Utils

class SubscriptionListAdapter(
    var activity: Activity,
    private val subscriptionList: List<SubscriptionListData>,
    private val onItemSelected: (SubscriptionListData) -> Unit,
    private val onItemDeleted: (SubscriptionListData) -> Unit
) :
    RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemSubscriptionBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = subscriptionList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return subscriptionList.size
    }

    inner class ViewHolder(val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscription: SubscriptionListData) {

            binding.tvFlowerName.text = subscription.flowerName
            if (subscription.flowerTeluguName.isNotEmpty()) binding.tvFlowerTeluguName.text =
                subscription.flowerTeluguName
            else binding.tvFlowerTeluguName.visibility = View.GONE

            Glide.with(binding.ivFlowerPhoto.context)
                .load(subscription.flowerImageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)

            binding.tvQuantity.text =
                activity.getString(R.string.quantity_subscription, subscription.qty)
            binding.tvStartDate.text = Utils().convertDate(subscription.subscriptionStartDate)

            binding.cardItem.setOnClickListener {
                onItemSelected.invoke(subscription)
            }

            binding.ivDeleteSubscription.setOnClickListener {
                onItemDeleted.invoke(subscription)
            }
        }
    }
}