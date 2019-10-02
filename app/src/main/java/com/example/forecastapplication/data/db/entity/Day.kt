package com.example.forecastapplication.data.db.entity


import androidx.room.Embedded
import com.example.forecastapplication.data.db.entity.Condition
import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("maxtemp_c")
    val maxtempC: Double,
    @SerializedName("maxtemp_f")
    val maxtempF: Double,
    @SerializedName("mintemp_c")
    val mintempC: Double,
    @SerializedName("mintemp_f")
    val mintempF: Double,
    @SerializedName("avgtemp_c")
    val avgtempC: Double,
    @SerializedName("avgtemp_f")
    val avgtempF: Double,
    @SerializedName("maxwind_mph")
    val maxwindMph: Double,
    @SerializedName("maxwind_kph")
    val maxwindKph: Double,
    @SerializedName("totalprecip_mm")
    val totalprecipMm: Double,
    @SerializedName("totalprecip_in")
    val totalprecipIn: Double,
    @SerializedName("avgvis_km")
    val avgvisKm: Double,
    @SerializedName("avgvis_miles")
    val avgvisMiles: Double,
    @SerializedName("avghumidity")
    val avghumidity: Double,
    @Embedded(prefix = "condition_")
    val condition: Condition
)