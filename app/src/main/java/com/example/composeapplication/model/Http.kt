package com.example.composeapplication.model

import android.annotation.SuppressLint
import com.example.composeapplication.const.Constants
import com.example.composeapplication.service.APIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Http private constructor() {
    val gank: APIService

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.GANK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        gank = retrofit.create(APIService::class.java)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: Http? = null

        fun getInstance(): Http {
            if (sInstance == null) {
                synchronized(Http::class.java) {
                    if (sInstance == null) {
                        sInstance = Http()
                    }
                }
            }
            return sInstance!!
        }
    }
}