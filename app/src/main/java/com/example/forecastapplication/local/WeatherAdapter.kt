package com.example.forecastapplication.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastapplication.R
import com.example.forecastapplication.data.unitlocalaized.future.simple.UnitSpecificShortFutureWeatherEntry
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class WeatherAdapter(
    private val weatherList: List<UnitSpecificShortFutureWeatherEntry>,
    private val isMetric: Boolean
): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){

    class WeatherViewHolder(item: View): RecyclerView.ViewHolder(item){
        val textViewTemperature = item.findViewById<TextView>(R.id.textView_temperature)
        val textViewCondition = item.findViewById<TextView>(R.id.textView_condition)
        val textViewDate = item.findViewById<TextView>(R.id.textView_date)
        val imageViewCondition = item.findViewById<ImageView>(R.id.imageView_condition_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.future_weather_list_item, null)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        val temperatureUnit = chooseUnitAbberviation(isMetric, "°C", "°F")
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

        holder.textViewTemperature.text = "${weather.avgTemperature}$temperatureUnit"
        holder.textViewDate.text = weather.date.format(dtFormatter)
        holder.textViewCondition.text = weather.conditionText
        //add weather icon
    }

    override fun getItemCount() = weatherList.size
}