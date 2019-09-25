package com.example.forecastapplication.data.network.response


import com.google.gson.annotations.SerializedName

data class Forecastday(
    @SerializedName("date")
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: Int,
    @SerializedName("day")
    val day: Day
)