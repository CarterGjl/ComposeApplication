package com.example.composeapplication.ui.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.composeapplication.model.Http
import com.example.composeapplication.ui.weather.WeatherImageFactory
import com.example.composeapplication.ui.weather.WeatherModel
import com.example.composeapplication.viewmodel.BaseViewModel

class WeatherViewModel : BaseViewModel() {

    val weatherLiveData = MutableLiveData<List<WeatherModel>>()

    fun getWeathers() {
        launch {
            val weatherResponse = Http.getInstance().gank.getWeathers()
            weatherResponse.result.forEach {
                it.imgRes = WeatherImageFactory.getWeatherImage(it.weather)
            }
            weatherLiveData.value = weatherResponse.result
        }
    }

}