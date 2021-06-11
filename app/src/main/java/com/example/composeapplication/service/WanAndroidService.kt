package com.example.composeapplication.service

import com.example.composeapplication.bean.LoginResponse
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import com.google.gson.JsonObject
import retrofit2.http.*

interface WanAndroidService {
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

    @FormUrlEncoded
    @POST("https://www.wanandroid.com/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("https://www.wanandroid.com/article/query/0/json")
    suspend fun search(
        @Field("k") key: String,
    ): ResultData
}