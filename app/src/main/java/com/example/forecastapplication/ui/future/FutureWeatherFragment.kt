package com.example.forecastapplication.ui.future

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.forecastapplication.R
import com.example.forecastapplication.data.network.WeatherAPIService
import com.example.forecastapplication.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_future_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FutureWeatherFragment : ScopedFragment(){

    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_future_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders
            .of(this@FutureWeatherFragment)
            .get(FutureWeatherViewModel::class.java)

        bindUI()


    }

    private fun bindUI() = launch {
        val futureWeatherList = viewModel.futureWeather.await()

        futureWeatherList.observe(this@FutureWeatherFragment, Observer {futureWeather ->
            textView.text = futureWeather.toString()
        })
    }
}