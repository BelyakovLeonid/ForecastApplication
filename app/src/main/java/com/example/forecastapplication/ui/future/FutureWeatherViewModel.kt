package com.example.forecastapplication.ui.future

import android.util.Log
import com.example.forecastapplication.data.repository.Repository
import com.example.forecastapplication.local.lazyDeferred
import com.example.forecastapplication.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureWeatherViewModel: WeatherViewModel() {
    val futureWeather by lazyDeferred {
        Repository.getFutureWeather(LocalDate.now(), unitSystemIsMetric)
    }
}