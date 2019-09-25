package com.example.forecastapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import com.example.forecastapplication.data.db.WeatherDataBase
import com.example.forecastapplication.data.network.WeatherAPIService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForecastApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}