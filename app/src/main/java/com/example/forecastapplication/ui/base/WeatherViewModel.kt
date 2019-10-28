package com.example.forecastapplication.ui.base

import androidx.lifecycle.ViewModel
import com.example.forecastapplication.local.UnitSystem
import com.example.forecastapplication.data.providers.UnitsProvider
import com.example.forecastapplication.data.repository.Repository
import com.example.forecastapplication.local.lazyDeferred

abstract class WeatherViewModel: ViewModel() {

    private val unitSystem = UnitsProvider.getUnitSystem()

    val unitSystemIsMetric: Boolean
       get() = unitSystem == UnitSystem.M

    val weatherLocation by lazyDeferred{
        Repository.getWeatherLocation()
    }
}