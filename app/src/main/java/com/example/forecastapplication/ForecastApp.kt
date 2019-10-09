package com.example.forecastapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.forecastapplication.data.db.WeatherDataBase
import com.google.android.gms.location.FusedLocationProviderClient
import com.jakewharton.threetenabp.AndroidThreeTen

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
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        appContext = applicationContext
        dataBase = WeatherDataBase.invoke(this)
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        fusedLocationProviderClient = FusedLocationProviderClient(applicationContext)
    }
}