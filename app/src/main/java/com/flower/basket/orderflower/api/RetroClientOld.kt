package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

object RetroClientOld {
    private const val BASE_URL = "https://flowerbasket.bsite.net/"    // Old
//    private const val BASE_URL = "http://www.flowerbasket.somee.com/"   //New

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

//    private val interceptor = Interceptor { chain ->
//        var request: Request = chain.request()
//        request = request.newBuilder()
//            .addHeader("Content-Type", "application/json")
//            .build()
//        chain.proceed(request)
//    }

    val trustManager = CustomTrustManager() // Implement your custom TrustManager
    lateinit var sslContext: SSLContext

    init {
        try {
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustManager), SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    private val builder = OkHttpClient().newBuilder()
        .connectTimeout(50, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
//        .addInterceptor(interceptor)

    private val httpClient: OkHttpClient = builder.build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .build()
        )
        .client(httpClient)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}