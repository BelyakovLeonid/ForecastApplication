package com.example.forecastapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastapplication.ForecastApp.Companion.dataBase
import com.example.forecastapplication.data.db.unitlocalaized.current.CurrentWeatherEntry
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

    init {
        WeatherDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }/*
        WeatherDataSource.downloadedFutureWeather.observeForever { newFutureWeather ->
            persistFetchedFutureWeather(newFutureWeather)
        }*/
    }

    fun persistFetchedCurrentWeather(currentWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(currentWeather.currentWeatherEntry[0])
        }
    }

    //out нужен для того, чтобы можно было возвращать наследников класса (интерфейса) UnitSpecificCurrrentWeather
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext if (isMetric) currentWeatherDao.getCurrentWeather()
                else currentWeatherDao.getCurrentWeather()
        }
    }

    private suspend fun initWeather(){
        val location = currentWeatherDao.getLocationNonLive()

        if(location == null || LocationProvider.hasLocationChanged(location)){
            fetchCurrentWeather()
            //fetchFutureWeather()
            return
        }

        if(isFetchCurrentWeatherNeeded(location.zonedDateTime))
            fetchCurrentWeather()

        //if(isFetchFutureWeatherNeeded())
        //    fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        WeatherDataSource.fetchCurrentWeather(
            LocationProvider.getPreferredLocationString(),
            Locale.getDefault().language,
            UnitsProvider.getUnitSystem().name
        )
    }


    private fun isFetchCurrentWeatherNeeded(lastFetchedTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }

/*
    fun persistFetchedFutureWeather(futureWeather: FutureWeatherResponse){
        val today = LocalDate.now()
        GlobalScope.launch(Dispatchers.IO) {
        }
    }

    suspend fun getFutureWeather(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out List<UnitSpecificShortFutureWeatherEntry>>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext if (isMetric) futureWeatherDao.getFutureWeatherMetric(date)
                else futureWeatherDao.getFutureWeatherImperial(date)
        }
    }

    suspend fun getFutureWeatherByDate(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out UnitSpecificFutureWeatherEntry>{
        return withContext(Dispatchers.IO){
            initWeather()
            return@withContext if(isMetric) futureWeatherDao.getFutureWeatherByDayMetric(date)
                else futureWeatherDao.getFutureWeatherByDayImperial(date)
        }
    }

    suspend fun getWeatherLocation(): LiveData<LocationWeatherEntry>{
        return withContext(Dispatchers.IO){
            return@withContext locationWeatherDao.getLocation()
        }
    }


    private suspend fun fetchFutureWeather() {
        WeatherDataSource.fetchFutureWeather(
            LocationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }


    private fun isFetchFutureWeatherNeeded(): Boolean {
        val today = LocalDate.now()
        val actualDays = futureWeatherDao.countFutureWeather(today)
        return actualDays < FUTURE_DAYS_COUNT
   }*/
}