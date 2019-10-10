package com.example.forecastapplication.data.unitlocalaized.future.detail

import androidx.room.Embedded
import com.example.forecastapplication.data.db.entity.Condition
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

interface UnitSpecificFutureWeatherEntry {
    val date: LocalDate
    val maxTemperature: Double
    val minTemperature: Double
    val avgTemperature: Double
    val maxWind: Double
    val totalPrecip: Double
    val avgVisibility: Double
    val avgHumidity: Double
    val conditionText: String
    val conditionIconUrl: String
}