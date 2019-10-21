package com.example.forecastapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecastapplication.ForecastApp.Companion.dataBase
import com.example.forecastapplication.data.db.entity.FutureWeatherEntry
import com.example.forecastapplication.data.db.unitlocalaized.UnitSystem
import com.example.forecastapplication.data.db.unitlocalaized.current.CurrentWeatherEntry
import com.example.forecastapplication.data.db.unitlocalaized.current.LocationWeatherEntry
import com.example.forecastapplication.data.network.FORECAST_DAYS_COUNT
import com.example.forecastapplication.data.providers.LocationProvider
import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.example.forecastapplication.data.providers.UnitsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

object Repository {

    val currentWeatherDao = dataBase.currentWeatherDao()
    val futureWeatherDao = dataBase.futureWeatherDao()

    init {
        WeatherDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
        WeatherDataSource.downloadedFutureWeather.observeForever { newFutureWeather ->
            persistFetchedFutureWeather(newFutureWeather)
        }
    }

    fun persistFetchedCurrentWeather(currentWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            val weather = currentWeather.currentWeatherEntry[0].also {
                it.isMetric = UnitsProvider.getUnitSystem() == UnitSystem.M
            }
            currentWeatherDao.upsert(weather)
        }
    }

    fun persistFetchedFutureWeather(futureWeather: FutureWeatherResponse){
        val today = LocalDate.now()
        GlobalScope.launch(Dispatchers.IO) {
            futureWeatherDao.deleteOldEntries(today)
            futureWeatherDao.upsert(futureWeather.data)
        }
    }

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }

    suspend fun getWeatherLocation(): LiveData<LocationWeatherEntry>{
        return withContext(Dispatchers.IO){
            return@withContext currentWeatherDao.getLocation()
        }
    }

    suspend fun getFutureWeather(
        date: LocalDate
    ): LiveData<List<FutureWeatherEntry>>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext futureWeatherDao.getFutureWeather(date)
        }
    }

    suspend fun getFutureWeatherByDate(
        date: LocalDate
    ): LiveData<FutureWeatherEntry>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext futureWeatherDao.getFutureWeatherByDate(date)
        }
    }

    private suspend fun initWeather(){
        val location = currentWeatherDao.getLocationNonLive()
        val isMetricUnitSystem = currentWeatherDao.isMetricUnitSystem()

        if(location == null || LocationProvider.hasLocationChanged(location)){
            Log.d("MyTag1", " LocationChanged")
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if(isUnitSystemChanged(isMetricUnitSystem)){
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if(isFetchCurrentWeatherNeeded(location.zonedDateTime)){
            fetchCurrentWeather()
        }

        if(isFetchFutureWeatherNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        val location = LocationProvider.getPreferredLocationString()
        Log.d("MyTag2", "$location")
        WeatherDataSource.fetchCurrentWeather(
            location.first,
            location.second,
            location.third,
            Locale.getDefault().language,
            UnitsProvider.getUnitSystem().name
        )
    }

    private suspend fun fetchFutureWeather() {
        val location = LocationProvider.getPreferredLocationString()
        Log.d("MyTag2", "$location")
        WeatherDataSource.fetchFutureWeather(
            location.first,
            location.second,
            location.third,
            Locale.getDefault().language,
            UnitsProvider.getUnitSystem().name
        )
    }

    private fun isUnitSystemChanged(isMetric: Boolean): Boolean{
        Log.d("MyTag1", "isUnitSystemChanged()")
        return isMetric != (UnitsProvider.getUnitSystem() == UnitSystem.M)
    }

    private fun isFetchFutureWeatherNeeded(): Boolean {
        val today = LocalDate.now()
        val actualDays = futureWeatherDao.countFutureWeather(today)
        return actualDays < FORECAST_DAYS_COUNT
    }

    private fun isFetchCurrentWeatherNeeded(lastFetchedTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }




}