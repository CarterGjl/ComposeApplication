package com.example.composeapplication.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

enum class Result{
    Loading,
}
class WeatherForecastRepository {

    fun fetchWeatherForecast() = flow {
        emit(Result.Loading)
        delay(1000)
        emit("1")
    }
}