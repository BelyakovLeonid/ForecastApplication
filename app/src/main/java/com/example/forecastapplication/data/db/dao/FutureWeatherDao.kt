package com.example.forecastapplication.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(futureWeather: List<FutureWeatherEntry>)

    @Query("SELECT * FROM future_weather WHERE date(validDate) >= date(:startDate)")
    fun getFutureWeather(startDate: LocalDate): LiveData<List<FutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE validDate == :date")
    fun getFutureWeatherByDate(date: LocalDate): LiveData<FutureWeatherEntry>

    @Query("SELECT count() FROM future_weather WHERE validDate >= :startDate")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM future_weather WHERE validDate < :startDate")
    fun deleteOldEntries(startDate: LocalDate)
}