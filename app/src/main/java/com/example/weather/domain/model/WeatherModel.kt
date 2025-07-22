package com.example.weather.domain.model

data class WeatherModel(
    val city: String,
    val temperature: Double,
    val humidity: Int,
    val iconUrl: String
)