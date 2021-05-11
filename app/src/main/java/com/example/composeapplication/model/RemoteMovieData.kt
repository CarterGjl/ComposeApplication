package com.example.composeapplication.model

import android.annotation.SuppressLint
import android.content.Context
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.const.Constants
import com.example.composeapplication.service.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteMovieData private constructor(private val context: Context) {
    private val movieInterface: MovieService

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.OMDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        movieInterface = retrofit.create(MovieService::class.java)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: RemoteMovieData? = null

        fun getInstance(context: Context): RemoteMovieData {
            if (sInstance == null) {
                synchronized(RemoteMovieData::class.java) {
                    if (sInstance == null) {
                        sInstance = RemoteMovieData(context)
                    }
                }
            }
            return sInstance!!
        }
    }

    suspend fun getArticles(page: Int = 0): ResultData {
        return movieInterface.getArticles(page = page)
    }

    suspend fun getMovieByCoroutines(movieID: String): MoviePro {
        return movieInterface.requestDetailByCoroutines(movieID, Constants.OMDB_API_KEY)
    }
}