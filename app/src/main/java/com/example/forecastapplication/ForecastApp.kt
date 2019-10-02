package com.example.forecastapplication

import android.app.Application
import android.util.Log
import com.example.forecastapplication.data.db.WeatherDataBase
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.network.WeatherAPIService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class ForecastApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}