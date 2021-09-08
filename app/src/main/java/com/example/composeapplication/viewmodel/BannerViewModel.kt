package com.example.composeapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.composeapplication.bean.Banner
import com.example.composeapplication.model.Http

class BannerViewModel : BaseViewModel() {

    val banners = MutableLiveData<List<Banner>>()

    fun getBanners() {
        launch {
            val banner = Http.getInstance().gank.getBanner()
            banners.value = banner.data
        }
    }


}