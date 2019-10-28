package com.example.forecastapplication.data.providers

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.forecastapplication.ForecastApp
import com.example.forecastapplication.data.db.additional_classes.LocationWeatherEntry
import com.example.forecastapplication.local.LocationPermissionNotGrantedException
import com.example.forecastapplication.local.toDeferred
import kotlinx.coroutines.Deferred
import kotlin.math.abs

const val DEVICE_LOCATION_KEY = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION_KEY = "CUSTOM_LOCATION"

object LocationProvider {
    private val appContext = ForecastApp.appContext
    private val preferences =  ForecastApp.preferences
    private val fusedLocationProviderClient = ForecastApp.fusedLocationProviderClient

    suspend fun hasLocationChanged(lastWeatherLocation: LocationWeatherEntry): Boolean{
        val customLocationChanged = hasCustomLocationChanged(lastWeatherLocation)
        val deviceLocationChanged = try{
            Log.d("MyTag1", "changed")
            hasDeviceLocationChanged(lastWeatherLocation)
        }catch(e: LocationPermissionNotGrantedException){
            Log.d("MyTag1", "exception")
            false
        }
        Log.d("MyTag1", "$deviceLocationChanged $customLocationChanged")
        return deviceLocationChanged || customLocationChanged
    }

    //возвращает либо <null, lat, lon>, либо <cityName, null, null>
    suspend fun getPreferredLocationString(): Triple<String?, String?, String?>{
        if(isDeviceLocationUse()){
            try{
                Log.d("MyTag2", "${getLastDeviceLocation().await()}")
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return Triple(getCustomLocation(), null, null)
                return Triple(null, "${deviceLocation.latitude}", "${deviceLocation.longitude}")
            }catch(e: LocationPermissionNotGrantedException){
                return Triple(getCustomLocation(), null, null)
            }
        }else{
            return Triple(getCustomLocation(), null, null)
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: LocationWeatherEntry): Boolean{
        if(!isDeviceLocationUse())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        //сравниваем double поэтому лучше через пороговую величину
        val threshold  = 0.03
        return abs(deviceLocation.longitude - lastWeatherLocation.lon) > threshold
                || abs(deviceLocation.latitude - lastWeatherLocation.lat) > threshold
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: LocationWeatherEntry): Boolean{
        if(!isDeviceLocationUse()){
            val lastCustomLocation = getCustomLocation()
            return lastCustomLocation != lastWeatherLocation.cityName
        }
        return false
    }

    private fun getLastDeviceLocation(): Deferred<Location?> {
        //val newClient = LocationServices.getFusedLocationProviderClient(appContext)
        return if (hasLocationPermissionGranted()){
            fusedLocationProviderClient.lastLocation.toDeferred()
            //newClient.lastLocation.toDeferred()
        }

        else
            throw LocationPermissionNotGrantedException()
    }

    private fun isDeviceLocationUse(): Boolean{
        Log.d("MyTag1", "LocationUse - ${preferences.getBoolean(DEVICE_LOCATION_KEY, false)}")
        return preferences.getBoolean(DEVICE_LOCATION_KEY, false)
    }

    private fun getCustomLocation(): String?{
        return preferences.getString(CUSTOM_LOCATION_KEY, "Moscow")
    }

    private fun hasLocationPermissionGranted(): Boolean{
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}