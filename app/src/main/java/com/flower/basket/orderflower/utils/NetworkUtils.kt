package com.flower.basket.orderflower.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
                        return false
                    }
                    return activeNetworkInfo.type == 1 || activeNetworkInfo.type == 0
                }

                val activeNetwork = connectivityManager.activeNetwork
                if (activeNetwork != null) {
                    val networkCapabilities =
                        connectivityManager.getNetworkCapabilities(activeNetwork)
                    return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}