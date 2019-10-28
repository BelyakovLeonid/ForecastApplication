package com.example.forecastapplication.data.db.additional_classes

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

data class LocationWeatherEntry(
    val timeEpoch: Long,
    val cityName: String,
    val datetime: String,
    val timezone: String,
    val lon: Double,
    val lat: Double
){

    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(timeEpoch)
            val timeZoneId = ZoneId.of(timezone)
            return ZonedDateTime.ofInstant(instant, timeZoneId)
        }
    }