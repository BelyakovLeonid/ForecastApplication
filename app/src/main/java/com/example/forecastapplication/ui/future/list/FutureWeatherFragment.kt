package com.example.forecastapplication.ui.future.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastapplication.R
import com.example.forecastapplication.data.db.converters.DateToStringConverter
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.local.WeatherAdapter
import com.example.forecastapplication.ui.base.ScopedFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_future_weather.*
import kotlinx.coroutines.launch

class FutureWeatherFragment : ScopedFragment(){

    private lateinit var viewModel: FutureWeatherViewModel
    private lateinit var recyclerView: RecyclerView

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

        recyclerView = recyclerView_weather
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        bindUI()
    }

    private fun bindUI() = launch {
        val location = viewModel.weatherLocation.await()
        val futureWeatherList = viewModel.futureWeather.await()

        location.observe(this@FutureWeatherFragment, Observer {weatherLocation ->
            Log.d("New1", "1")
            if(weatherLocation == null)
                return@Observer

            Log.d("New1", "2")
            updateActionbar(weatherLocation.cityName)
        })

        futureWeatherList.observe(this@FutureWeatherFragment, Observer {futureWeather ->
            if(futureWeather == null) return@Observer

            group_loading.visibility = View.GONE
            val isMetric = viewModel.unitSystemIsMetric
            val adapter = WeatherAdapter(futureWeather, isMetric){weather ->
                showDetailWeather(weather)
            }
            recyclerView.adapter = adapter
        })
    }

    private fun updateActionbar(location: String){
        Log.d("New1", "3")
        (activity as? AppCompatActivity)?.supportActionBar?.let{
            Log.d("New1", "4")
            it.title = location
            it.subtitle = "Weather forecast"
        }
    }

    private fun showDetailWeather(futureWeather: FutureWeatherEntry){
        val dateString = futureWeather.validDate
        val action = FutureWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view!!).navigate(action)
    }
}