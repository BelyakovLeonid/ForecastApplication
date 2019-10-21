package com.example.forecastapplication.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.forecastapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.forecastapplication.ForecastApp
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.test.*

const val REQUEST_CHECK_SETTINGS = 3

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this,
            R.id.container_fragment
        )
        bottom_navigation.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CHECK_SETTINGS -> {
                val locationRequest = LocationRequest.create()?.apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                val locationCallback = object : LocationCallback(){
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        ForecastApp.fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
                ForecastApp.fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //для навигации с помощью стрелки "назад"
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}


