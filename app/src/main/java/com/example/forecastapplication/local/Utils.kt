package com.example.forecastapplication.local

fun chooseUnitAbberviation(isMetric: Boolean, metricUnit: String, imperialUnit: String): String{
    if(isMetric)
        return metricUnit
    else
        return imperialUnit
}