package com.example.forecastapplication.data.unitlocalaized.future

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class MetricFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempC")
    override val maxTemperature: Double,
    @ColumnInfo(name = "mintempC")
    override val minTemperature: Double,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "maxwindKph")
    override val maxWind: Double,
    @ColumnInfo(name = "totalprecipMm")
    override val totalPrecip: Double,
    @ColumnInfo(name = "avgvisKm")
    override val avgVisibility: Double,
    @ColumnInfo(name = "avghumidity")
    override val avgHumidity: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String
): UnitSpecificFutureWeatherEntry