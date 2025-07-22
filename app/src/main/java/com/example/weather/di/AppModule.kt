package com.example.weather.di



import com.example.weather.data.api.WeatherApi
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.presentation.viewmodel.ThemeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun provideGetWeatherUseCase(
        repository: WeatherRepository
    ): com.example.weather.domain.usecase.GetWeatherUseCase {
        return com.example.weather.domain.usecase.GetWeatherUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideThemeViewModel(): ThemeViewModel = ThemeViewModel()
}
