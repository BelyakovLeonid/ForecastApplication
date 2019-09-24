package com.example.forecastapplication.ui.future

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forecastapplication.R
import com.example.forecastapplication.ui.base.ScopedFragment

class FutureWeatherFragment : ScopedFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_future_weather, container, false)
    }
}