package com.example.forecastapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastapplication.data.network.WeatherAPIService
import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.example.forecastapplication.local.InternetConnectionException
import java.lang.Exception

const val FUTURE_DAYS_COUNT = 7

object  WeatherDataSource {
    private val apiService = WeatherAPIService()

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    suspend fun fetchCurrentWeather(location: String, languageCode: String){
        try{
            val value = apiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadedCurrentWeather.postValue(value)
        }catch(e: InternetConnectionException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    suspend fun fetchFutureWeather(location: String, languageCode: String){
        try{
            val value = apiService
                .getFutureWeather(location, languageCode, FUTURE_DAYS_COUNT)
                .await()
            _downloadedFutureWeather.postValue(value)
        }catch(e: InternetConnectionException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

}