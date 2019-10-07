package com.example.forecastapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastapplication.ForecastApp.Companion.dataBase
import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.example.forecastapplication.data.unitlocalaized.current.UnitSpecificCurrentWeatherEntry
import com.example.forecastapplication.data.unitlocalaized.future.UnitSpecificFutureWeatherEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

object Repository {

    val currentWeatherDao = dataBase.currentWeatherDao()
    val futureWeatherDao = dataBase.futureWeatherDao()
    val locationWeatherDao = dataBase.locationWeatherDao()

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
            currentWeatherDao.upsert(currentWeather.currentWeatherEntry)
            locationWeatherDao.upsert(currentWeather.location)
        }
    }

    fun persistFetchedFutureWeather(futureWeather: FutureWeatherResponse){
        val today = LocalDate.now()
        GlobalScope.launch(Dispatchers.IO) {
            futureWeatherDao.deleteOldEntries(today)
            futureWeatherDao.insert(futureWeather.forecastDaysContainer.entries)
            locationWeatherDao.upsert(futureWeather.location)
        }
    }

    //out нужен для того, чтобы можно было возвращать наследников класса (интерфейса) UnitSpecificCurrrentWeather
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>{
        initWeather()
        return withContext(Dispatchers.IO){
            return@withContext if (isMetric) currentWeatherDao.getCurrentWeatherMetric()
                else currentWeatherDao.getCurrentWeatherImperial()
        }
    }

    suspend fun getFutureWeather(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out List<UnitSpecificFutureWeatherEntry>>{
        initWeather()
        return withContext(Dispatchers.IO){
            return@withContext if (isMetric) futureWeatherDao.getFutureWeatherMetric(date)
                else futureWeatherDao.getFutureWeatherImperial(date)
        }
    }

    suspend fun getFutureWeatherByDate(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out UnitSpecificFutureWeatherEntry>{
        initWeather()
        return withContext(Dispatchers.IO){
            return@withContext if(isMetric) futureWeatherDao.getFutureWeatherByDayMetric(date)
                else futureWeatherDao.getFutureWeatherByDayImperial(date)
        }
    }

    private suspend fun initWeather(){
        // сюда еще добавить, что если локация изменилась, то мы тоже обновляем погоду
        val location = locationWeatherDao.getLocationNonLive()

        if(isFetchCurrentWeatherNeeded(location.zonedDateTime))
            fetchCurrentWeather()

        if(isFetchFutureWeatherNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        WeatherDataSource.fetchCurrentWeather("Samara", "en")
    }

    private suspend fun fetchFutureWeather() {
        WeatherDataSource.fetchFutureWeather("Samara", "en")
    }

    private fun isFetchCurrentWeatherNeeded(lastFetchedTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureWeatherNeeded(): Boolean {
        val today = LocalDate.now()
        val actualDays = futureWeatherDao.countFutureWeather(today)
        return actualDays >= FUTURE_DAYS_COUNT
    }

}