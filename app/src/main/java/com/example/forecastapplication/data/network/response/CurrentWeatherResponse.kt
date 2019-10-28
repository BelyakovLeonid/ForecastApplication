package com.example.forecastapplication.data.network.response


import com.example.forecastapplication.data.db.entity.CurrentWeatherAndLocationEntry
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("data")
    val currentWeatherEntry: List<CurrentWeatherAndLocationEntry>,
    val count: Int
)