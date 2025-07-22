package com.example.weather.data.repository

import com.example.weather.data.api.WeatherApi
import com.example.weather.data.mapper.toDomain
import com.example.weather.domain.model.ForecastItem

import com.example.weather.domain.model.WeatherModel
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeather(city: String, apiKey: String): WeatherModel {
        return api.getWeatherByCity(city, apiKey).toDomain()
    }

    override suspend fun getForecast(city: String, apiKey: String): List<ForecastItem> {
        return api.getForecast(city, apiKey).list.map {
            ForecastItem(
                date = it.dt_txt,
                temperature = it.main.temp,
                iconUrl = "https://openweathermap.org/img/wn/${it.weather.firstOrNull()?.icon}.png"
            )
        }
    }

}