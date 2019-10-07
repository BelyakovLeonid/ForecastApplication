package com.example.forecastapplication.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.LocationWeatherEntry
import com.example.forecastapplication.data.db.entity.WEATHER_LOCATION_ID

@Dao
interface LocationWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(location: LocationWeatherEntry)

    @Query("SELECT * FROM location_weather WHERE id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<LocationWeatherEntry>

    @Query("SELECT * FROM location_weather WHERE id = $WEATHER_LOCATION_ID")
    fun getLocationNonLive(): LocationWeatherEntry?
}