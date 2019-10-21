package com.example.forecastapplication.local

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

fun chooseUnitAbberviation(isMetric: Boolean, metricUnit: String, imperialUnit: String): String{
    if(isMetric)
        return metricUnit
    else
        return imperialUnit
}
