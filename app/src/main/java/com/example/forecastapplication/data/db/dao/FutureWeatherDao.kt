package com.example.forecastapplication.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.detail.ImperialFutureWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.detail.MetricFutureWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.simple.ImperialShortFutureWeartherEntry
import com.example.forecastapplication.data.unitlocalaized.future.simple.MetricShortFutureWeatherEntry
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("SELECT * FROM future_weather WHERE date(date) > date(:startDate)")
    fun getFutureWeatherMetric(startDate: LocalDate): LiveData<List<MetricShortFutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) > date(:startDate)")
    fun getFutureWeatherImperial(startDate: LocalDate): LiveData<List<ImperialShortFutureWeartherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) = date(:selectedDate)")
    fun getFutureWeatherByDayMetric(selectedDate: LocalDate): LiveData<MetricFutureWeatherEntry>

    @Query("SELECT * FROM future_weather WHERE date(date) = date(:selectedDate)")
    fun getFutureWeatherByDayImperial(selectedDate: LocalDate): LiveData<ImperialFutureWeatherEntry>

    @Query("SELECT count(id) FROM future_weather WHERE date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM future_weather WHERE date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}