package com.example.forecastapplication.ui.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.forecastapplication.R
import com.example.forecastapplication.data.db.unitlocalaized.current.LocationWeatherEntry
import com.example.forecastapplication.local.chooseUnitAbberviation
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


        locationWeather.observe(this@CurrentWeatherFragment, Observer{ location->
            if(location == null) return@Observer
            Log.d("MyTag11", "${location.zonedDateTime}")
            updateLocation(location)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {weatherEntry ->
            if(weatherEntry == null) return@Observer

            group_loading.visibility = View.GONE //GONE - вью невидима и НЕ занимает места в разметке
            // INVISIBLE - вью невидима, но ЗАНИМАЕТ место в разметке

            updateDateToToday()
            updateConditionText(weatherEntry.weatherDescription)
            updateConditionIcon(weatherEntry.weatherIcon)
            updateTemperatures(weatherEntry.temperature, weatherEntry.feelsLikeTemperature)
            updatePrecipitiation(weatherEntry.precipitationVolume)
            updateWind(weatherEntry.windSpeed, weatherEntry.windDirection)
            updateVisibility(weatherEntry.visibility)
            updateSunMovement(weatherEntry.sunrise, weatherEntry.sunset)
        })
    }


    private fun updateLocation(location: LocationWeatherEntry){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location.cityName
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Weather today"
    }

    private fun updateConditionText(conditionText: String){
        textView_condition.text = conditionText
    }

    private fun updateConditionIcon(iconCode: String){
        val resId = this.resources.getIdentifier(iconCode, "drawable", activity?.packageName)

        Glide.with(this)
            .load(resId)
            .into(imageView_condition_icon)
    }

    private fun updateTemperatures(temperature: Double, temperatureFeelsLike: Double){
        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric,"°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like: $temperatureFeelsLike$unitAbbreviation"
    }

    private fun updatePrecipitiation(precipitation: Double){
        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric,"mm", "in")
        textView_precipitation.text = "Precipitation: $precipitation $unitAbbreviation"
    }

    private fun updateWind(windSpeed: Double, windDirection: String){
        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric,"Kph", "Mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric,"km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updateSunMovement(sunrise: String, sunset: String){
        textView_sunrise.text = "Sunrise: $sunrise"
        textView_sunset.text = "Sunset: $sunset"
    }

}