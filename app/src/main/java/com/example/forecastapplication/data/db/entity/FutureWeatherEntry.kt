package com.example.forecastapplication.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "future_weather", indices = [Index(value = ["validDate"], unique = true)])
data class FutureWeatherEntry(
    @SerializedName("pres")
    val pressure: Double,
    @SerializedName("high_temp")
    val highTemp: Double,
    @SerializedName("sunset_ts")
    val sunsetTs: Long,
    @SerializedName("sunrise_ts")
    val sunriseTs: Long,
    @SerializedName("app_min_temp")
    val appMinTemp: Double,
    @SerializedName("wind_spd")
    val windSpeed: Double,
    @SerializedName("wind_cdir_full")
    val windDirection: String,
    @SerializedName("slp")
    val slp: Double,
    @SerializedName("valid_date")
    val validDate: String,
    @SerializedName("app_max_temp")
    val appMaxTemp: Double,
    @SerializedName("vis")
    val visibility: Double,
    @Embedded(prefix = "weather_")
    val weather: Weather,
    @SerializedName("precip")
    val precip: Double,
    @SerializedName("low_temp")
    val lowTemp: Double,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("min_temp")
    val minTemp: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}