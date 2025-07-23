package com.example.weather.data.mapper

import com.example.weather.domain.model.WeatherModel
import com.example.weather.data.api.WeatherResponseDto


fun WeatherResponseDto.toDomain(): WeatherModel {
    val icon = weather.firstOrNull()?.icon ?: "01d" // fallback to sunny icon
    return WeatherModel(
        city = name,
        temperature = main.temp,
        humidity = main.humidity,
        feelsLike = main.feels_like,
        pressure = main.pressure,
        windSpeed = wind.speed,
        iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
    )
}