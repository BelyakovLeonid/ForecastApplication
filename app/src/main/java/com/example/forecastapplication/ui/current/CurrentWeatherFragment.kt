package com.example.forecastapplication.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.forecastapplication.R
import com.example.forecastapplication.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.launch

class CurrentWeatherFragment : ScopedFragment() {

    private lateinit var viewModel: CurrentWeatherViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.currentWeather.await()
        val locationWeather = viewModel.weatherLocation.await()

        currentWeather.observe(this@CurrentWeatherFragment, Observer {weatherEntry ->
            if(weatherEntry == null) return@Observer

            textView.text = weatherEntry.toString()
        })
    }
}