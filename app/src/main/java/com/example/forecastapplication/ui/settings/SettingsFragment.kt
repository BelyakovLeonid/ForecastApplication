package com.example.forecastapplication.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.forecastapplication.R
import android.content.pm.PackageManager
import android.Manifest.permission.*
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.forecastapplication.ForecastApp
import com.example.forecastapplication.ForecastApp.Companion.PACKAGE_NAME
import com.example.forecastapplication.ui.REQUEST_CHECK_SETTINGS
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar


const val LOCATION_KEY = "USE_DEVICE_LOCATION"
const val LOCATION_REQUEST_CODE = 1
const val REQUEST_SETTINGS = 2

class SettingsFragment: PreferenceFragmentCompat(){

    lateinit var locationPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        locationPreference = preferenceManager.findPreference(LOCATION_KEY)!!
        initLocationPreference()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val title = resources.getString(R.string.settings_title)
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_SETTINGS -> {
                if(checkHasLocationPermission())
                    locationPreference.isChecked = true
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            LOCATION_REQUEST_CODE ->{
                if(grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ){
                    val locationPreference = preferenceManager.findPreference<SwitchPreference>(LOCATION_KEY)
                    locationPreference?.isChecked = true
                }else{
                    val message = resources.getString(R.string.settings_snackbar_text_one)
                    makeSnackbar(message)
                }
            }
        }
    }

    private fun initLocationPreference() {
        //если permission было отозвано, то нужно убрать бегунок
        val hasPermission = checkHasLocationPermission()
        if(!hasPermission)
            locationPreference.isChecked = false

        //при нажатии на бегунок, он переместится только когда есть permission (если его нет, оно будет запрошено)
        locationPreference.setOnPreferenceChangeListener {_, _ ->
            checkGPSSettings()

            if(checkHasLocationPermission()){
                ForecastApp.activateLocationService()
                return@setOnPreferenceChangeListener true
            }else{
                if(shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)){
                        val message = resources.getString(R.string.settings_snackbar_text_two)
                        makeSnackbar(message)
                }else{
                    requestPermissions(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
                }
                return@setOnPreferenceChangeListener false
            }
        }
    }


    private fun checkGPSSettings(){
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(activity!!)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(activity!!, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("NewTag", "some exception")
                }
            }
        }
    }

    private fun openSettings() {
        val appSettingsIntent = Intent().also{
            it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            it.data = Uri.parse("package:$PACKAGE_NAME")
        }
        startActivityForResult(appSettingsIntent, REQUEST_SETTINGS)
    }

    private fun checkHasLocationPermission(): Boolean{
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context!!,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasFineLocation = ContextCompat.checkSelfPermission(context!!,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return hasCoarseLocation && hasFineLocation
    }

    private fun makeSnackbar(message: String){
        val actionText = resources.getString(R.string.settings_snackbar_action_text)
        val toastText = resources.getString(R.string.settings_toast_text)

        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
            .setAction(actionText){
                openSettings()

                Toast.makeText(context,
                    toastText,
                    Toast.LENGTH_LONG)
                    .show()
            }
            .setActionTextColor(ResourcesCompat.getColor(resources, R.color.green, null))
            .show()
    }

}