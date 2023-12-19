package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.BuildConfig
import com.flower.basket.orderflower.FlowerBasketApp
import com.flower.basket.orderflower.data.preference.AppPersistence
import com.flower.basket.orderflower.data.preference.AppPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroClient {
//    private const val BASE_URL = "https://flowerbasket.bsite.net/"    // Old
//    private const val BASE_URL = "http://www.flowerbasket.somee.com/"   //New

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val interceptor = Interceptor { chain ->
        var request: Request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader(
                headerName(),
                "${headerPrecedence()} ${AppPreference(FlowerBasketApp.context!!).getPreference(AppPersistence.keys.AUTH_TOKEN) as String}"
            )
            .build()
        chain.proceed(request)
    }

    private val builder = OkHttpClient().newBuilder()
        .connectTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(interceptor)

    private val httpClient: OkHttpClient = builder.build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(AppData.getData(baseURL()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    private external fun baseURL(): String
    private external fun headerName(): String
    private external fun headerPrecedence(): String
}