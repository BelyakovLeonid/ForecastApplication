package com.example.forecastapplication.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.forecastapplication.ForecastApp
import com.example.forecastapplication.R
import com.example.forecastapplication.data.db.additional_classes.LocationWeatherEntry
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
        ForecastApp.activateLocationService()

        val currentWeather = viewModel.currentWeather.await()
        val locationWeather = viewModel.weatherLocation.await()


        locationWeather.observe(this@CurrentWeatherFragment, Observer{ location->
            if(location == null) return@Observer
            updateActionbar(location)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {weatherEntry ->
            if(weatherEntry == null) return@Observer

            group_loading.visibility = View.GONE //GONE - вью невидима и НЕ занимает места в разметке
            // INVISIBLE - вью невидима, но ЗАНИМАЕТ место в разметке

            updateConditionText(weatherEntry.weatherDescription)
            updateConditionIcon(weatherEntry.weatherIcon)
            updateTemperatures(weatherEntry.temperature, weatherEntry.feelsLikeTemperature)
            updatePrecipitation(weatherEntry.precipitationVolume)
            updateWind(weatherEntry.windSpeed, weatherEntry.windDirection)
            updateVisibility(weatherEntry.visibility)
            updateSunMovement(weatherEntry.sunrise, weatherEntry.sunset)
        })
    }


    private fun updateActionbar(location: LocationWeatherEntry){
        val subtitle = resources.getString(R.string.current_subtitle)
        (activity as? AppCompatActivity)?.supportActionBar?.title = location.cityName
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = subtitle
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
        val metricAbbrev = resources.getString(R.string.unit_metric_temperature)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_temperature)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_temperature.text = resources.getString(R.string.temperature_text, temperature, unitAbbreviation)
        textView_feels_like_temperature.text = resources.getString(R.string.temperature_feels_like_text, temperatureFeelsLike, unitAbbreviation)
    }

    private fun updatePrecipitation(precipitation: Double){
        val metricAbbrev = resources.getString(R.string.unit_metric_precipitation)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_precipitation)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_precipitation.text = resources.getString(R.string.precipitation_text, precipitation, unitAbbreviation)
    }

    private fun updateWind(windSpeed: Double, windDirection: String){
        val metricAbbrev = resources.getString(R.string.unit_metric_wind_speed)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_wind_speed)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_wind.text = resources.getString(R.string.wind_text, windDirection, windSpeed, unitAbbreviation)
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val metricAbbrev = resources.getString(R.string.unit_metric_visibility)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_visibility)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_visibility.text = resources.getString(R.string.visibility_text, visibilityDistance, unitAbbreviation)
    }

    private fun updateSunMovement(sunrise: String, sunset: String){
        textView_sunrise.text = resources.getString(R.string.sunrise_text, sunrise)
        textView_sunset.text = resources.getString(R.string.sunset_text, sunset)
    }

}