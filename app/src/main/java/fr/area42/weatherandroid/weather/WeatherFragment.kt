package fr.area42.weatherandroid.weather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import fr.area42.weatherandroid.App
import fr.area42.weatherandroid.R
import fr.area42.weatherandroid.openweathermap.WeatherWrapper
import fr.area42.weatherandroid.openweathermap.mapOpenWeatherDataToWeather
import fr.area42.weatherandroid.utils.Convert
import fr.area42.weatherandroid.utils.execute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WeatherFragment : Fragment() {
    companion object {
        val EXTRA_CITY_NAME = "fr.area42.weatherandroid.extras.EXTRA_CITY_NAME"
        fun newInstance() = WeatherFragment()
    }

    private val TAG = WeatherFragment::class.java.simpleName
    private lateinit var cityName: String
    private lateinit var locale: String

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var city: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        city = view.findViewById(R.id.city)
        weatherIcon = view.findViewById<ImageView>(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature_data)
        humidity = view.findViewById(R.id.humidity_data)
        pressure = view.findViewById(R.id.pressure_data)
        locale = Locale.getDefault().language

        swipeRefresh.setOnRefreshListener { refreshWeather() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity?.intent!!.hasExtra(EXTRA_CITY_NAME)) {
            updateWeatherForCity(activity!!.intent.getStringExtra(EXTRA_CITY_NAME))
        }
    }

    fun updateWeatherForCity(cityName: String) {
        this.cityName = cityName

        if (!swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = true
        }

        val call = App.weatherService.getWeather("$cityName,fr", locale)
        call.enqueue(object: Callback<WeatherWrapper> {
            override fun onResponse(call: Call<WeatherWrapper>?, response: Response<WeatherWrapper>?) {
                response?.body()?.let {
                    val weather = mapOpenWeatherDataToWeather(it)
                    updateUi(weather)
                }
                Log.i(TAG, "OpenWeatherMap response: ${response?.body()}")
                swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.e(TAG, getString(R.string.could_not_load_city_weather), t)
                Toast.makeText(activity,
                    getString(R.string.could_not_load_city_weather),
                    Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun updateUi(weather: Weather) {

        Picasso.get()
            .load(weather.iconUrl)
            .placeholder(R.drawable.cloud_off_outline)
            .into(weatherIcon)

        city.text = cityName
        weatherDescription.text = weather.description
        temperature.text = getString(R.string.weather_celsius_temperature_value, execute(weather.temperature, Convert.ToCelsius).toDouble())
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
        pressure.text = getString(R.string.weather_pressure_value, weather.pressure)
    }

    private fun refreshWeather() {
        updateWeatherForCity(cityName)
    }
}