package com.example.forecastapplication.ui.future.detail

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
import com.example.forecastapplication.data.db.converters.DateToStringConverter
import com.example.forecastapplication.local.chooseUnitAbberviation
import com.example.forecastapplication.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_future_detail_weather.*
import kotlinx.android.synthetic.main.fragment_future_detail_weather.imageView_condition_icon
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_precipitation
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_sunrise
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_sunset
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_temperature
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_visibility
import kotlinx.android.synthetic.main.fragment_future_detail_weather.textView_wind
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import kotlin.math.round
import kotlin.math.roundToInt

class FutureWeatherDetailFragment: ScopedFragment() {
    private lateinit var zoneId: String
    private lateinit var localDate: LocalDate
    private lateinit var viewModel: FutureWeatherDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_future_detail_weather, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs = arguments?.let {bundle -> FutureWeatherDetailFragmentArgs.fromBundle(bundle)}
        localDate = DateToStringConverter.stringToDate(safeArgs?.dateString)!!
        val viewModelFactory = FutureWeatherDetailViewModelFactory(localDate)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FutureWeatherDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(this@FutureWeatherDetailFragment, Observer{location ->
            if(location == null)
                return@Observer

            zoneId = location.timezone
            updateLocation(location.cityName)
        })

        val detailedWeather = viewModel.detailFutureWeather.await()
        detailedWeather.observe(this@FutureWeatherDetailFragment, Observer { weather ->
            if(weather == null)
                return@Observer

            updateDate(localDate)
            updateConditionText(weather.weather.description)
            updateConditionIcon(weather.weather.icon)
            updateTemperatures(weather.temp, weather.maxTemp,weather.minTemp)
            updateWind(weather.windDirection, weather.windSpeed)
            updatePrecipitation(weather.precip)
            updateVisibility(weather.visibility)
            updateSunMovement(weather.sunriseTs, weather.sunsetTs)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: LocalDate) {
        val dateString = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            resources.getString(R.string.forecast_detail_subtitle, dateString)
    }

    private fun updateConditionText(condition: String){
        textView_condition.text = condition
    }

    private fun updateConditionIcon(iconCode: String){
        val resId = this.resources.getIdentifier(iconCode, "drawable", activity?.packageName)

        Glide.with(this)
            .load(resId)
            .into(imageView_condition_icon)
    }

    private fun updateTemperatures(avgTemp: Double, maxTemp: Double, minTemp: Double) {
        val metricAbbrev = resources.getString(R.string.unit_metric_temperature)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_temperature)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_temperature.text = resources.getString(R.string.temperature_text, avgTemp, unitAbbreviation)
        textView_maxTemp.text = resources.getString(R.string.temperature_maximum_text, maxTemp, unitAbbreviation)
        textView_minTemp.text = resources.getString(R.string.temperature_minimum_text, minTemp, unitAbbreviation)
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val roundedSpeed = round(windSpeed * 10) / 10
        val metricAbbrev = resources.getString(R.string.unit_metric_wind_speed)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_wind_speed)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_wind.text = resources.getString(R.string.wind_text, windDirection, windSpeed, unitAbbreviation)
    }

    private fun updatePrecipitation(precip: Double) {
        val metricAbbrev = resources.getString(R.string.unit_metric_precipitation)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_precipitation)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_precipitation.text = resources.getString(R.string.precipitation_text, precip, unitAbbreviation)
    }

    private fun updateVisibility(visibility: Double) {
        val metricAbbrev = resources.getString(R.string.unit_metric_visibility)
        val imperialAbbrev = resources.getString(R.string.unit_imperial_visibility)

        val unitAbbreviation = chooseUnitAbberviation(viewModel.unitSystemIsMetric, metricAbbrev, imperialAbbrev)
        textView_visibility.text = resources.getString(R.string.visibility_text, visibility, unitAbbreviation)
    }

    private fun updateSunMovement(sunriseTs: Long, sunsetTs: Long) {
        val instantSunrise = Instant.ofEpochSecond(sunriseTs)
        val instantSunset = Instant.ofEpochSecond(sunsetTs)
        val timeZoneId = ZoneId.of(zoneId)
        val sunrise = ZonedDateTime.ofInstant(instantSunrise, timeZoneId).format(DateTimeFormatter.ofPattern("HH:mm"))
        val sunset = ZonedDateTime.ofInstant(instantSunset, timeZoneId).format(DateTimeFormatter.ofPattern("HH:mm"))

        textView_sunrise.text = resources.getString(R.string.sunrise_text, sunrise)
        textView_sunset.text = resources.getString(R.string.sunset_text, sunset)
    }
}