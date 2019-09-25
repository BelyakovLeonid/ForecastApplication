package com.example.forecastapplication.ui.future

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forecastapplication.R
import com.example.forecastapplication.data.WeatherAPIService
import com.example.forecastapplication.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_future_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FutureWeatherFragment : ScopedFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_future_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val apiService = WeatherAPIService()

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.getFutureWeather("Samara").await()
            textView.text = response.toString()
        }
    }
}