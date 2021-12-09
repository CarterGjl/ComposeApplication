package com.example.composeapplication.repository

import kotlinx.coroutines.flow.flow

enum class Result{
    Loading,
}
class WeatherForecastRepository {

    fun fetchWeatherForecast() = flow {
        emit(Result.Loading)
        kotlinx.coroutines.delay(1000)
        emit("1")
    }
}