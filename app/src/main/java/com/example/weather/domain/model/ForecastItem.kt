package com.example.weather.domain.model

data class ForecastItem(
    val date: String,
    val temperature: Double,
    val iconUrl: String
)