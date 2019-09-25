package com.example.forecastapplication.data

import com.example.forecastapplication.data.network.response.CurrentWeatherResponse
import com.example.forecastapplication.data.network.response.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "89e8bd89085b41b7a4b142029180210"

//http://api.apixu.com/v1/current.json?key=89e8bd89085b41b7a4b142029180210&q=London&lang=en
//https://api.apixu.com/v1/forecast.json?key=89e8bd89085b41b7a4b142029180210&q=Los%20Angeles&days=7

interface WeatherAPIService {

    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") language: String = "en"
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast.json")
    fun getFutureWeather(
        @Query("q") location: String,
        @Query("lang") language: String = "en",
        @Query("days") days: Int = 7
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

            val client = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.apixu.com/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(WeatherAPIService::class.java)
        }
    }
}