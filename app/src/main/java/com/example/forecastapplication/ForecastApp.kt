package com.example.forecastapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.forecastapplication.data.db.WeatherDataBase
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.network.WeatherAPIService
import com.example.forecastapplication.data.providers.PreferenceProvider
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class ForecastApp: Application() {

    companion object{
        lateinit var appContext: Context
        lateinit var dataBase: WeatherDataBase
        lateinit var preferences: SharedPreferences

        //думаю, что не сильно страшно
        @SuppressLint("StaticFieldLeak")
        lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        dataBase = WeatherDataBase.invoke(this)
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        fusedLocationProviderClient = FusedLocationProviderClient(applicationContext)
    }
}