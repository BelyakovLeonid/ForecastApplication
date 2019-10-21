package com.example.forecastapplication.ui.future.detail

import com.example.forecastapplication.data.repository.Repository
import com.example.forecastapplication.local.lazyDeferred
import com.example.forecastapplication.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureWeatherDetailViewModel(
    detailDate: LocalDate
): WeatherViewModel() {

    val detailFutureWeather by lazyDeferred{
         Repository.getFutureWeatherByDate(detailDate)
    }
}