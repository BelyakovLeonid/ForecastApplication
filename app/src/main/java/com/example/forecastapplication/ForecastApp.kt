package com.example.forecastapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.forecastapplication.data.db.WeatherDataBase
import com.example.forecastapplication.data.providers.LocationProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jakewharton.threetenabp.AndroidThreeTen

class ForecastApp: Application() {
    private lateinit var locationCallback: LocationCallback

    companion object{
        lateinit var appContext: Context
        lateinit var dataBase: WeatherDataBase
        lateinit var preferences: SharedPreferences
        lateinit var PACKAGE_NAME: String

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
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
        PACKAGE_NAME = packageName

    }



}