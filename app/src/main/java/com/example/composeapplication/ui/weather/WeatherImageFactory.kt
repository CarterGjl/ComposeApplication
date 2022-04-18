package com.example.composeapplication.ui.weather

import com.example.composeapplication.R


object WeatherImageFactory {

    fun getWeatherImage(weather: String): Int {
        return when (weather) {
            "多云", "多云转阴", "多云转晴" -> {
                R.mipmap.biz_plugin_weather_duoyun
            }
            "中雨", "中到大雨" -> {
                R.mipmap.biz_plugin_weather_zhongyu
            }
            "雷阵雨" -> {
                R.mipmap.biz_plugin_weather_leizhenyu
            }
            "阵雨", "阵雨转多云" -> {
                R.mipmap.biz_plugin_weather_zhenyu
            }
            "暴雪" -> {
                R.mipmap.biz_plugin_weather_baoxue
            }
            "暴雨" -> {
                R.mipmap.biz_plugin_weather_baoyu
            }
            "大暴雨" -> {
                R.mipmap.biz_plugin_weather_dabaoyu
            }
            "大雪" -> {
                R.mipmap.biz_plugin_weather_daxue
            }
            "大雨", "大雨转中雨" -> {
                R.mipmap.biz_plugin_weather_dayu
            }
            "雷阵雨冰雹" -> {
                R.mipmap.biz_plugin_weather_leizhenyubingbao
            }
            "晴" -> {
                R.mipmap.biz_plugin_weather_qing
            }
            "沙尘暴" -> {
                R.mipmap.biz_plugin_weather_shachenbao
            }
            "特大暴雨" -> {
                R.mipmap.biz_plugin_weather_tedabaoyu
            }
            "雾", "雾霾" -> {
                R.mipmap.biz_plugin_weather_wu
            }
            "小雪" -> {
                R.mipmap.biz_plugin_weather_xiaoxue
            }
            "小雨" -> {
                R.mipmap.biz_plugin_weather_xiaoyu
            }
            "阴" -> {
                R.mipmap.biz_plugin_weather_yin
            }
            "雨夹雪" -> {
                R.mipmap.biz_plugin_weather_yujiaxue
            }
            "阵雪" -> {
                R.mipmap.biz_plugin_weather_zhenxue
            }
            "中雪" -> {
                R.mipmap.biz_plugin_weather_zhongxue
            }
            else -> {
                R.mipmap.biz_plugin_weather_duoyun
            }
        }
    }
}