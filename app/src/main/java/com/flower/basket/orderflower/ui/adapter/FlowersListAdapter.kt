package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.FlowerData
import com.flower.basket.orderflower.databinding.ItemFlowersBinding
import com.flower.basket.orderflower.utils.SubscriptionType

class FlowersListAdapter(
    var activity: Activity,
    private val flowersList: ArrayList<FlowerData>,
    private val onItemSelected: (FlowerData, SubscriptionType) -> Unit
) :
    RecyclerView.Adapter<FlowersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemFlowersBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = flowersList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return flowersList.size
    }

    inner class ViewHolder(val binding: ItemFlowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flower: FlowerData) {
            binding.tvFlowerName.text = flower.name
            if (flower.teluguName.isNotEmpty()) binding.tvFlowerTeluguName.text = flower.teluguName
            else binding.tvFlowerTeluguName.visibility = View.GONE

            binding.tvFlowerPrice.text =
                activity.getString(R.string.rupee_symbol, flower.loosePrice.toDouble())

            Glide.with(binding.ivFlowerPhoto.context)
                .load(flower.imageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)

            binding.cardItem.setOnClickListener {
                onItemSelected.invoke(flower, SubscriptionType.Subscribe)
            }

            binding.btnSubscribe.setOnClickListener {
                onItemSelected.invoke(flower, SubscriptionType.Subscribe)
            }

            binding.btnBuyOnce.setOnClickListener {
                onItemSelected.invoke(flower, SubscriptionType.BuyOnce)
            }
        }
    }
}