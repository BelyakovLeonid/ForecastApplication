package com.example.forecastapplication.data.db.additional_classes

import androidx.room.ColumnInfo

data class CurrentWeatherEntry(
    val temperature: Double,
    val feelsLikeTemperature: Double,
    val partOfDay: String,
    val pressure: Double,
    val windSpeed: Double,
    val windDirection: String,
    val visibility: Double,
    val precipitationVolume: Double,
    val sunset: String,
    val sunrise: String,
    @ColumnInfo(name = "weather_icon")
    val weatherIcon: String,
    @ColumnInfo(name = "weather_code")
    val wearherCode: String,
    @ColumnInfo(name = "weather_description")
    val weatherDescription: String
)
