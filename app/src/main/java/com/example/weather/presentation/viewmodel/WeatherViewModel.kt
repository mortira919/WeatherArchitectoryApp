package com.example.weather.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.model.WeatherModel
import com.example.weather.domain.model.ForecastItem
import com.example.weather.domain.usecase.GetWeatherUseCase
import com.example.weather.domain.usecase.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    var weather by mutableStateOf<WeatherModel?>(null)
        private set

    var forecast by mutableStateOf<List<ForecastItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var lastUpdatedTime by mutableStateOf("")
        private set

    fun loadWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                weather = getWeatherUseCase(city, apiKey)
                forecast = getForecastUseCase(city, apiKey)
                lastUpdatedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                Log.d("WeatherViewModel", "Погода получена: $weather")
            } catch (e: Exception) {
                e.printStackTrace()
                error = "Ошибка: ${e.message}"
            }

            isLoading = false
        }
    }
}