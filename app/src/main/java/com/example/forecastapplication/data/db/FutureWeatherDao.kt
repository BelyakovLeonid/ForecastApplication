package com.example.forecastapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.ImperialFutureWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.MetricFutureWeatherEntry
import org.threeten.bp.LocalDate
import java.util.*

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("SELECT * FROM future_weather WHERE date(date) > date(:startDate)")
    fun getFutureWeatherMetric(startDate: LocalDate): LiveData<List<MetricFutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) > date(:startDate)")
    fun getFutureWeatherImperial(startDate: LocalDate): LiveData<List<ImperialFutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) = date(:selectedDate)")
    fun getFutureWeatherByDayMetric(selectedDate: LocalDate): LiveData<MetricFutureWeatherEntry>

    @Query("SELECT * FROM future_weather WHERE date(date) = date(:selectedDate)")
    fun getFutureWeatherByDayImperial(selectedDate: LocalDate): LiveData<ImperialFutureWeatherEntry>

    @Query("SELECT count(id) FROM future_weather WHERE date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM future_weather WHERE date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}