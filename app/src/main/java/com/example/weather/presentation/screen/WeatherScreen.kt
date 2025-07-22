package com.example.weather.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weather.presentation.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    var city by remember { mutableStateOf("Almaty") }
    val apiKey = "7de9cb86647eefd9a6bdaca678609776"
    val context = LocalContext.current

    var isDarkTheme by remember { mutableStateOf(false) }
    var animateThemeChange by remember { mutableStateOf(false) }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (animateThemeChange) 0f else 1f,
        animationSpec = tween(durationMillis = 250), label = "fade"
    )

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Прогноз погоды") },
                    actions = {
                        IconButton(onClick = {
                            animateThemeChange = true
                        }) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness4,
                                contentDescription = "Toggle Theme",
                                modifier = Modifier.alpha(animatedAlpha)
                            )
                        }

                        LaunchedEffect(animateThemeChange) {
                            if (animateThemeChange) {
                                delay(500)
                                isDarkTheme = !isDarkTheme
                                animateThemeChange = false
                            }
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Введите город") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.loadWeather(city, apiKey) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Показать погоду")
                    }

                    Button(
                        onClick = {
                            getCityNameFromCoords(context) { detectedCity ->
                                if (detectedCity != null) {
                                    city = detectedCity
                                    viewModel.loadWeather(city, apiKey)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Моё местоположение")
                    }

                    if (viewModel.isLoading) {
                        CircularProgressIndicator()
                    }

                    viewModel.weather?.let { weather ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Город: ${weather.city}", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    painter = rememberAsyncImagePainter(weather.iconUrl),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Температура: ${weather.temperature}°C", fontSize = 18.sp)
                                Text("Влажность: ${weather.humidity}%", fontSize = 18.sp)
                                Text(
                                    "Последнее обновление: ${viewModel.lastUpdatedTime}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    if (viewModel.forecast.isNotEmpty()) {
                        Text("Прогноз на несколько дней:", fontSize = 18.sp)

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(viewModel.forecast) { item ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(width = 120.dp, height = 160.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(item.date, fontSize = 14.sp)
                                        Image(
                                            painter = rememberAsyncImagePainter(item.iconUrl),
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("${item.temperature}°C", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }

                    viewModel.error?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        )
    }
}

@SuppressLint("MissingPermission")
fun getCityNameFromCoords(context: Context, onResult: (String?) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            location?.let {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val cityName = addresses?.firstOrNull()?.locality
                onResult(cityName)
            } ?: onResult(null)
        }
        .addOnFailureListener {
            onResult(null)
        }
}
