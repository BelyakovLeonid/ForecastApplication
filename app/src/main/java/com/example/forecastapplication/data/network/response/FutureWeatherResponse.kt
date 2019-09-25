package com.example.forecastapplication.data.network.response


import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: CurrentWeatherEntry,
    @SerializedName("forecast")
    val forecast: Forecast
)