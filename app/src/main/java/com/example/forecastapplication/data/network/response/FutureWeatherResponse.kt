package com.example.forecastapplication.data.network.response


import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("data")
    val data: List<FutureWeatherEntry>,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("lat")
    val lat: String
)