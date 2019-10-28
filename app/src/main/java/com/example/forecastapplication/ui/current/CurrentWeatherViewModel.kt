package com.example.forecastapplication.ui.current

import com.example.forecastapplication.data.repository.Repository
import com.example.forecastapplication.local.lazyDeferred
import com.example.forecastapplication.ui.base.WeatherViewModel

class CurrentWeatherViewModel: WeatherViewModel(){
    val currentWeather by lazyDeferred {
        Repository.getCurrentWeather()
    }
}