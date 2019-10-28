package com.example.forecastapplication.data.providers

import com.example.forecastapplication.ForecastApp
import com.example.forecastapplication.local.UnitSystem

const val UNITS_KEY = "UNIT_SYSTEM"

object UnitsProvider {
    private val preferences =  ForecastApp.preferences

    fun getUnitSystem(): UnitSystem {
        val unitSystem = preferences.getString(UNITS_KEY, UnitSystem.M.name)
        return UnitSystem.valueOf(unitSystem!!)
    }
}