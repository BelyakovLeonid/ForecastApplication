package com.example.forecastapplication.data.network

import android.util.Log
import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



const val API_KEY =   "d41a3797795c451a8dfc9a6e7f845946"
const val FORECAST_DAYS_COUNT = 7

//http://api.weatherbit.io/v2.0/current?key=d41a3797795c451a8dfc9a6e7f845946&city=Moscow
//https://api.weatherbit.io/v2.0/forecast/daily?city=Moscow&key=d41a3797795c451a8dfc9a6e7f845946

interface WeatherAPIService {

    @GET("current")
    fun getCurrentWeather(
        @Query("city") city: String = "Samara",
        @Query("lang") language: String = "en",
        @Query("units") units: String = "M"
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast/daily")
    fun getFutureWeather(
        @Query("city") city: String = "Samara",
        @Query("lang") language: String = "en",
        @Query("units") units: String = "M",
        @Query("days") days: Int = FORECAST_DAYS_COUNT
    ): Deferred<FutureWeatherResponse>

    companion object{
        operator fun invoke(): WeatherAPIService{
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val interceptor = HttpLoggingInterceptor{message ->
                Log.d("MyTag", message) }
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("http://api.weatherbit.io/v2.0/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(WeatherAPIService::class.java)
        }
    }
}