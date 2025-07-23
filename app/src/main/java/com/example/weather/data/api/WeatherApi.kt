package com.example.weather.data.api

import com.example.weather.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

// --- Ответ от сервера OpenWeatherMap
data class WeatherResponseDto(
    val name: String,
    val main: MainDto,
    val wind: WindDto,                    // ← новое поле
    val weather: List<WeatherDto>
)

data class MainDto(
    val temp: Double,
    val humidity: Int,
    val pressure: Int,       // ← новое поле
    val feels_like: Double   // ← новое поле
)

data class WindDto(
    val speed: Double        // ← новое поле
)

data class WeatherDto(
    val icon: String,
    val description: String
)

// --- API-интерфейс
interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru"
    ): WeatherResponseDto


    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse
}
