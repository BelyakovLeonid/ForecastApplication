package com.example.forecastapplication.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather_location")
data class CurrentWeatherAndLocationEntry(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("app_temp")
    val feelsLikeTemperature: Double,
    @SerializedName("pod")
    val partOfDay: String,
    @SerializedName("pres")
    val pressure: Double,
    @SerializedName("wind_spd")
    val windSpeed: Double,
    @SerializedName("wind_cdir_full")
    val windDirection: String,
    @SerializedName("vis")
    val visibility: Double,
    @SerializedName("precip")
    val precipitationVolume: Double,
    val sunset: String,
    val sunrise: String,
    @Embedded(prefix = "weather_")
    val weather: Weather,
    @SerializedName("ts")
    val timeEpoch: Long,
    val datetime: String,
    val timezone: String,
    @SerializedName("city_name")
    val cityName: String,
    val lon: Double,
    val lat: Double
){
    @PrimaryKey(autoGenerate = false)
    var id = CURRENT_WEATHER_ID
    var isMetric: Boolean = true
}