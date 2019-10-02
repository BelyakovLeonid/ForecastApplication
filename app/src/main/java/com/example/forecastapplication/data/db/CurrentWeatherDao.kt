package com.example.forecastapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecastapplication.data.db.entity.CurrentWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.current.ImperialCurrentWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.current.MetricCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherEntry: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherMetric(): LiveData<MetricCurrentWeatherEntry>

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}