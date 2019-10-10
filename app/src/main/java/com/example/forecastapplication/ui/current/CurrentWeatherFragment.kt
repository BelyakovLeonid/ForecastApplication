package com.example.forecastapplication.ui.current

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.forecastapplication.R
import com.example.forecastapplication.data.db.entity.LocationWeatherEntry
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
            updateLocation(location)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {weatherEntry ->
            if(weatherEntry == null) return@Observer

            group_loading.visibility = View.GONE //GONE - вью невидима и НЕ занимает места в разметке
                                                 // INVISIBLE - вью невидима, но ЗАНИМАЕТ место в разметке

            updateDateToToday()
            updateConditionText(weatherEntry.conditionText)
            updateConditionIcon(weatherEntry.conditionIconUrl)
            updateTemperatures(weatherEntry.temperature, weatherEntry.feelsLikeTemperature)
            updatePreciptiation(weatherEntry.precipitationVolume)
            updateWind(weatherEntry.windSpeed, weatherEntry.windDirection)
            updateVisibility(weatherEntry.visibilityDistance)
        })
    }


    private fun updateLocation(location: LocationWeatherEntry){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location.name
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Weather today"
    }

    private fun updateConditionText(conditionText: String){
        textView_condition.text = conditionText
    }

    private fun updateConditionIcon(conditionIconURL: String){
        //здесь надо загрузить картинку в imageView с помощью Glide
    }

    private fun updateTemperatures(temperature: Double, temperatureFeelsLike: Double){
        val unitAbberviation = chooseLocalizedUnitsAbberviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbberviation"
        textView_feels_like_temperature.text = "$temperatureFeelsLike$unitAbberviation"
    }

    private fun updateWind(windSpeed: Double, windDirection: String){
        val unitAbberviation = chooseLocalizedUnitsAbberviation("Kph", "Mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbberviation"
    }

    private fun updatePreciptiation(precipitation: Double){
        val unitAbberviation = chooseLocalizedUnitsAbberviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitation $unitAbberviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitsAbberviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun chooseLocalizedUnitsAbberviation(metric: String, imperial: String): String{
        if (viewModel.unitSystemIsMetric)
            return metric
        else
            return imperial
    }
}