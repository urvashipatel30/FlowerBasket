package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.FlowerData
import com.flower.basket.orderflower.databinding.ItemFlowersItemBinding
import com.flower.basket.orderflower.utils.BuyOption

class FlowersListAdapter(
    var activity: Activity,
    private val flowersList: ArrayList<FlowerData>,
    private val onItemSelected: (FlowerData, BuyOption) -> Unit
) :
    RecyclerView.Adapter<FlowersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemFlowersItemBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = flowersList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return flowersList.size
    }

    inner class ViewHolder(val binding: ItemFlowersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flower: FlowerData) {
            binding.tvFlowerName.text = flower.name
            binding.tvFlowerTeluguName.text = flower.teluguName

            binding.tvFlowerPrice.text = "₹${flower.loosePrice}"

            Glide.with(binding.ivFlowerPhoto.context)
                .load(flower.imageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)

            binding.cardItem.setOnClickListener {
                onItemSelected.invoke(flower, BuyOption.Subscribe)
            }

            binding.btnSubscribe.setOnClickListener {
                onItemSelected.invoke(flower, BuyOption.Subscribe)
            }

            binding.btnBuyOnce.setOnClickListener {
                onItemSelected.invoke(flower, BuyOption.BuyOnce)
            }
        }
    }
}