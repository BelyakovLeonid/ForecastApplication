package com.example.forecastapplication.data.db

import android.content.Context
import androidx.room.*
import com.example.forecastapplication.data.db.converters.DateToStringConverter
import com.example.forecastapplication.data.db.dao.CurrentWeatherDao
import com.example.forecastapplication.data.db.entity.CurrentWeatherAndLocationEntry


const val DATA_BASE_NAME = "weather_database.db"

@Database(
    entities = [CurrentWeatherAndLocationEntry::class],
    version = 1
)
@TypeConverters(DateToStringConverter::class)
abstract class WeatherDataBase:RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object{
        @Volatile private var instance: WeatherDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): WeatherDataBase = instance ?: synchronized(LOCK){
            instance?: buildDataBase(context).also { instance = it }
        }

        private fun buildDataBase(context: Context): WeatherDataBase{
            return Room.databaseBuilder(
                context,
                WeatherDataBase::class.java,
                DATA_BASE_NAME
            ).build()
        }
    }
}