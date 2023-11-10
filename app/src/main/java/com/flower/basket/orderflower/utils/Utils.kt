package com.flower.basket.orderflower.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class Utils {

    fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.getDefault())

        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

   fun convertDateToISO8601(inputDate: String): String {
       val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
       val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.ENGLISH)

        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    fun convertFromOrderDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("E, MMM dd yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}