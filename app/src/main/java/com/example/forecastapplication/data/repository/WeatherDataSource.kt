package com.example.forecastapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastapplication.data.network.FORECAST_DAYS_COUNT
import com.example.forecastapplication.data.network.WeatherAPIService
import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.example.forecastapplication.local.InternetConnectionException

object  WeatherDataSource {
    private val apiService = WeatherAPIService()

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    suspend fun fetchCurrentWeather(
        location: String?,
        latitude: String?,
        longitude: String?,
        languageCode: String,
        units: String
    ){
        try{
            val value = apiService
                .getCurrentWeather(location, latitude, longitude, languageCode, units)
                .await()
            _downloadedCurrentWeather.postValue(value)
        }catch(e: InternetConnectionException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    suspend fun fetchFutureWeather(
        location: String?,
        latitude: String?,
        longitude: String?,
        languageCode: String,
        units: String
    ){
        try{
            val value = apiService
                .getFutureWeather(location, latitude, longitude, languageCode,units, FORECAST_DAYS_COUNT)
                .await()
            _downloadedFutureWeather.postValue(value)
        }catch(e: InternetConnectionException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

}