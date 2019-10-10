package com.example.forecastapplication.data.unitlocalaized.future.simple

import org.threeten.bp.LocalDate

interface UnitSpecificShortFutureWeatherEntry {
    val date: LocalDate
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
}