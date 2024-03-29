@file:Suppress("unused")

package com.example.composeapplication.service

import com.example.composeapplication.bean.HotKeyResult
import com.example.composeapplication.bean.LoginResponse
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import retrofit2.http.*

interface WanAndroidService {

    @GET("https://wanandroid.com/article/listproject/0/json")
    suspend fun requestSearchByCoroutines(
        @Query("s") keywords: String,
        @Query("apikey") apikey: String
    ): ResultData

    @GET("https://www.wanandroid.com/article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): ResultData

    @GET("https://www.wanandroid.com/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ResultData

    @GET("https://www.wanandroid.com//hotkey/json")
    suspend fun getHotKeys(): HotKeyResult

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

    /**
     * 知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("https://www.wanandroid.com/tree/json")
    suspend fun getTypeTreeList(): TreeListResponse
}