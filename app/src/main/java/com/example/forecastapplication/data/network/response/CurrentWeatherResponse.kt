package com.example.forecastapplication.data.network.response


import com.example.forecastapplication.data.db.entity.CurrentWeatherEntry
import com.example.forecastapplication.data.db.entity.LocationWeatherEntry
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val location: LocationWeatherEntry,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)