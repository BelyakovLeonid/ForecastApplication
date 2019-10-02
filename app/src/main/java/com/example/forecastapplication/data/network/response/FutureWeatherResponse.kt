package com.example.forecastapplication.data.network.response


import com.example.forecastapplication.data.db.entity.CurrentWeatherEntry
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    val location: Location,
    val current: CurrentWeatherEntry,
    @SerializedName("forecast")
    val forecastDaysContainer: ForecastDaysContainer
)