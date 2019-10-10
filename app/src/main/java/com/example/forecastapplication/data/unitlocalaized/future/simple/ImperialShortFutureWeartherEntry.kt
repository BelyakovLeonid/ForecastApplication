package com.example.forecastapplication.data.unitlocalaized.future.simple

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialShortFutureWeartherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String
) : UnitSpecificShortFutureWeatherEntry