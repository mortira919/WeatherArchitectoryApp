package com.example.weather.domain.repository

import com.example.weather.domain.model.ForecastItem
import com.example.weather.domain.model.WeatherModel

interface WeatherRepository {
    suspend fun getWeather(city: String, apiKey: String): WeatherModel
    suspend fun getForecast(city: String, apiKey: String): List<ForecastItem>
}