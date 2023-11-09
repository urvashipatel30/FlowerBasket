package com.flower.basket.orderflower.ui.adapter

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.data.Day

class DaysAdapter(private val daysList: List<Day>, var daySelectionCallback: DaySelectionCallback) :
    RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    interface DaySelectionCallback {
        fun onDaysSelected(selectedDays: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_days_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val day = daysList[position]
        holder.tvDayName.text = day.name
        holder.tvDayName.isSelected = day.selected
        holder.cbDay.isChecked = day.selected

        holder.itemView.setOnClickListener {
            if (day.value == -1) { // "All" option
                for (otherDay in daysList) {
                    otherDay.selected = true
                }
            } else {
                // Toggle the selection status of the clicked day
                day.selected = !day.selected

                // If any individual day is deselected, uncheck the "All" option
                val isAnyDayDeselected = daysList.any { it.value != -1 && !it.selected }
                daysList.first { it.value == -1 }.selected = !isAnyDayDeselected
            }

            notifyItemRangeChanged(0, daysList.size)

            // Build the final string with selected day values separated by commas
            val selectedDays = daysList.filter { it.selected && it.value != -1 }
                .joinToString(",") { it.value.toString() }

            // Notify the activity about the selected days
            daySelectionCallback.onDaysSelected(selectedDays)
        }
    }

    override fun getItemCount(): Int = daysList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDayName: TextView = itemView.findViewById(R.id.tvDayName)
        val cbDay: CheckBox = itemView.findViewById(R.id.cbDay)
    }
}