package com.example.composeapplication.service

import com.example.composeapplication.bean.*
import com.example.composeapplication.ui.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface APIService {

    companion object {
        //API均来自网上开放Api接口，仅供交流学习，切勿商用
        const val NEWS_URL = "4/news/latest"
        const val MOVIE_URL =
            "http://baobab.kaiyanapp.com/api/v4/rankList/videos"
        const val PIC_URL = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/21"
        const val WEATHER_URL =
            "http://api.k780.com/?app=weather.future&weaId=169&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json"
    }

    @GET("https://gank.io/api/v2/banners")
    suspend fun getBanner(): BannerResult

    @GET(NEWS_URL)
    suspend fun getNews(): NewsModelModel

    @GET("data/category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun getPics(
        @Path("page") page: Int = 1,
        @Path("count") count: Int = 21
    ): PageModel<List<PictureModel>>
    @GET("data/category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun getPics1(
        @Path("page") page: Int = 1,
        @Path("count") count: Int = 21
    ): PageModel<List<DataGirl>>

    @GET
    suspend fun getWeathers(@Url url: String = WEATHER_URL): WeatherResponse
}