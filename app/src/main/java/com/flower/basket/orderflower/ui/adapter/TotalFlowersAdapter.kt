package com.flower.basket.orderflower.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.totalflowers.TotalFlowersData
import com.flower.basket.orderflower.databinding.ItemTotalFlowersBinding
import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.Quantity

class TotalFlowersAdapter(
    var activity: Activity,
    private val totalFlowersList: ArrayList<TotalFlowersData>
) :
    RecyclerView.Adapter<TotalFlowersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemTotalFlowersBinding.inflate(LayoutInflater.from(activity), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flowerItem = totalFlowersList[position]
        holder.bind(flowerItem)
    }

    override fun getItemCount(): Int {
        return totalFlowersList.size
    }

    inner class ViewHolder(val binding: ItemTotalFlowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flower: TotalFlowersData) {

//            binding.tvFlowerName.text = if (report.flowerTeluguName.isNotEmpty())  report.flowerTeluguName else report.flowerName
            binding.tvFlowerName.text = flower.flowerTeluguName.ifEmpty { flower.flowerName }
            binding.tvFlowerName.requestFocus()

            binding.tvLooseQty.text =
                if (flower.totalLooseQty != 0) "${flower.totalLooseQty * Quantity.GRAMS.value}" else "-"
            binding.tvMoraQty.text = if (flower.totalMoraQty != 0) "${flower.totalMoraQty}" else "-"

            Glide.with(binding.ivFlowerPhoto.context)
                .load(flower.flowerImageUrl)
                .placeholder(R.drawable.ic_profile_holder)
                .error(R.drawable.ic_profile_holder)
                .centerCrop()
                .into(binding.ivFlowerPhoto)
        }
    }
}