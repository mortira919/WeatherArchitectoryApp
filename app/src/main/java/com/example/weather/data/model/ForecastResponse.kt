package com.example.weather.data.model

data class ForecastResponse(
    val list: List<ForecastData>
)

data class ForecastData(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val icon: String)