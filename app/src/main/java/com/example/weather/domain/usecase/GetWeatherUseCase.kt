package com.example.weather.domain.usecase

import com.example.weather.domain.model.WeatherModel
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String, apiKey: String): WeatherModel {
        return repository.getWeather(city, apiKey)
    }
}