package com.example.composeapplication.model

import android.annotation.SuppressLint
import com.example.composeapplication.bean.HotKeyResult
import com.example.composeapplication.bean.LoginResponse
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.const.Constants
import com.example.composeapplication.service.WanAndroidService
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import retrofit2.http.Query

class RemoteSevice private constructor() {
    private val wanAndroidInterface: WanAndroidService

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.OMDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        wanAndroidInterface = retrofit.create(WanAndroidService::class.java)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: RemoteSevice? = null

        fun getInstance(): RemoteSevice {
            if (sInstance == null) {
                synchronized(RemoteSevice::class.java) {
                    if (sInstance == null) {
                        sInstance = RemoteSevice()
                    }
                }
            }
            return sInstance!!
        }
    }

    suspend fun getArticles(page: Int = 0): ResultData {
        return wanAndroidInterface.getArticles(page = page)
    }

    suspend fun getArticleList(
        page: Int,
        cid: Int
    ): ResultData {
        return wanAndroidInterface.getArticleList(page = page, cid = cid)
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return wanAndroidInterface.login(username, password)
    }

    suspend fun searchArticle(key: String): ResultData {
        return wanAndroidInterface.search(key = key)
    }

    suspend fun getTypeTreeList(): TreeListResponse {
        return wanAndroidInterface.getTypeTreeList()
    }

    suspend fun getHotKeys(): HotKeyResult {
        return wanAndroidInterface.getHotKeys()
    }

    @Suppress("unused")
    suspend fun getMovieByCoroutines(movieID: String): MoviePro {
        return wanAndroidInterface.requestDetailByCoroutines(movieID, Constants.OMDB_API_KEY)
    }
}