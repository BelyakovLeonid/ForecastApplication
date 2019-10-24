package com.example.forecastapplication.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecastapplication.data.db.entity.CurrentWeatherAndLocationEntry
import com.example.forecastapplication.data.db.additional_classes.CurrentWeatherEntry
import com.example.forecastapplication.data.db.additional_classes.LocationWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherEntry: CurrentWeatherAndLocationEntry)

    @Query("SELECT * FROM current_weather_location WHERE id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherEntry>

    @Query("SELECT * FROM current_weather_location WHERE id = $CURRENT_WEATHER_ID")
    fun getLocation(): LiveData<LocationWeatherEntry>

    @Query("SELECT * FROM current_weather_location WHERE id = $CURRENT_WEATHER_ID")
    fun getLocationNonLive(): LocationWeatherEntry?

    @Query("SELECT isMetric FROM current_weather_location WHERE id = $CURRENT_WEATHER_ID")
    fun isMetricUnitSystem(): Boolean
}