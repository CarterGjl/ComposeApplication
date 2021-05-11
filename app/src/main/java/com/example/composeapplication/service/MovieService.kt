package com.example.composeapplication.service

import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    //
    @GET("https://wanandroid.com/article/listproject/0/json")
    suspend fun requestSearchByCoroutines(
//        @Query("s") keywords: String,
//        @Query("apikey") apikey: String
    ): ResultData

    @GET("http://ombapi.com/")
    suspend fun requestDetailByCoroutines(
        @Query("i") id: String,
        @Query("apikey") apikey: String
    ): MoviePro

    @GET("https://wanandroid.com/article/listproject/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): ResultData
}