package fr.area42.weatherandroid.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "e645456044265162e6785392aa3c30e3"

interface OpenWeatherService {
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") cityName: String,
                   @Query("lang") lang: String = "fr",
                   @Query("appid") apiKey: String = API_KEY) : Call<WeatherWrapper>
}