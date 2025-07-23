package com.example.weather.domain.model

data class WeatherModel(
    val city: String,
    val temperature: Double,
    val humidity: Int,
    val iconUrl: String,
    val feelsLike: Double,
    val pressure: Int,
    val windSpeed: Double

)