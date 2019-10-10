package com.example.forecastapplication.data.unitlocalaized.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempF")
    override val maxTemperature: Double,
    @ColumnInfo(name = "mintempF")
    override val minTemperature: Double,
    @ColumnInfo(name = "avgtempF")
    override val avgTemperature: Double,
    @ColumnInfo(name = "maxwindMph")
    override val maxWind: Double,
    @ColumnInfo(name = "totalprecipIn")
    override val totalPrecip: Double,
    @ColumnInfo(name = "avgvisMiles")
    override val avgVisibility: Double,
    @ColumnInfo(name = "avghumidity")
    override val avgHumidity: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String
): UnitSpecificFutureWeatherEntry