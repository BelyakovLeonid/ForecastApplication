package com.example.forecastapplication.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.forecastapplication.ForecastApp.Companion.PACKAGE_NAME
import com.example.forecastapplication.R
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.db.unitlocalaized.current.CurrentWeatherEntry
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class WeatherAdapter(
    private val weatherList: List<FutureWeatherEntry>,
    private val isMetric: Boolean,
    private val onClick: (weather: FutureWeatherEntry)-> Unit
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
        val iconResId = holder.itemView.resources.getIdentifier(
            weather.weather.icon,
            "drawable",
            PACKAGE_NAME)

        holder.textViewTemperature.text = "${weather.temp}$temperatureUnit"
        holder.textViewDate.text = weather.validDate.format(dtFormatter)
        holder.textViewCondition.text = weather.weather.description
        Glide.with(holder.itemView).load(iconResId).into(holder.imageViewCondition)
        holder.itemView.setOnClickListener {
            onClick.invoke(weather)
        }
    }

    override fun getItemCount() = weatherList.size
}