package com.flower.basket.orderflower.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.flower.basket.orderflower.data.community.CommunityData

class CommunityAdapter (
    context: Context,
    resource: Int,
    private val communityList: List<CommunityData>
) : ArrayAdapter<String>(context, resource) {

    override fun getCount(): Int {
        return communityList.size
    }

    override fun getItem(position: Int): String? {
        return communityList[position].name
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}