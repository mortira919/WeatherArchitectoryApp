package com.example.weather.domain.usecase

import com.example.weather.domain.model.ForecastItem
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String, apiKey: String): List<ForecastItem> {
        return repository.getForecast(city, apiKey)
    }
}