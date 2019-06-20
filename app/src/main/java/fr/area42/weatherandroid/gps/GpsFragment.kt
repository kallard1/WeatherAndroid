package fr.area42.weatherandroid.gps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import fr.area42.weatherandroid.App
import fr.area42.weatherandroid.R
import fr.area42.weatherandroid.openweathermap.WeatherWrapper
import fr.area42.weatherandroid.openweathermap.mapOpenWeatherDataToWeather
import fr.area42.weatherandroid.service.GpsService
import fr.area42.weatherandroid.utils.Convert
import fr.area42.weatherandroid.utils.execute
import fr.area42.weatherandroid.utils.toast
import fr.area42.weatherandroid.weather.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*



class GpsFragment : Fragment() {
    private lateinit var gpsBroadcastReceiver: GpsBroadcastReceiver

    companion object {
        val TAG = GpsActivity::class.java.simpleName
        val BROADCAST_ACTION = "fr.area42.weatherandroid.gps"
        fun newInstance() = GpsFragment()
    }

    private lateinit var cityName: String
    private lateinit var locale: String

    private lateinit var toolbar: Toolbar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        weatherIcon = view.findViewById(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature_data)
        humidity = view.findViewById(R.id.humidity_data)
        pressure = view.findViewById(R.id.pressure_data)
        locale = Locale.getDefault().language

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startService()
        gpsBroadcastReceiver = GpsBroadcastReceiver()
        registerGpsReceiver()

        return view
    }

    private fun startService() {
        val intentService = Intent(activity, GpsService::class.java)
        activity.startService(intentService)
    }

    private fun registerGpsReceiver() {
        try {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_ACTION)
            activity.registerReceiver(gpsBroadcastReceiver, intentFilter)
            Log.d(TAG, "Receiver registered!")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun updateUi(weather: Weather) {
        Picasso.get()
            .load(weather.iconUrl)
            .placeholder(R.drawable.cloud_off_outline)
            .into(weatherIcon)

        toolbar.title = "Demo"
        weatherDescription.text = weather.description
        temperature.text = getString(
            R.string.weather_celsius_temperature_value,
            execute(weather.temperature, Convert.ToCelsius).toDouble()
        )
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
        pressure.text = getString(R.string.weather_pressure_value, weather.pressure)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unregisterReceiver(gpsBroadcastReceiver)
    }

    inner class GpsBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                Log.d(TAG, "onReceive() called")

                val lon = intent?.getDoubleExtra("lon", 0.0)
                val lat = intent?.getDoubleExtra("lat", 0.0)

                Log.d(TAG, "lat ${lat} - lon ${lon}")
                val call = App.weatherService.getWeatherByLocation(lat, lon)
                call.enqueue(object : Callback<WeatherWrapper> {
                    /**
                     * Invoked for a received HTTP response.
                     *
                     *
                     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                     * Call [Response.isSuccessful] to determine if the response indicates success.
                     */
                    override fun onResponse(call: Call<WeatherWrapper>?, response: Response<WeatherWrapper>?) {
                        response?.body()?.let {
                            val weather = mapOpenWeatherDataToWeather(it)
                            updateUi(weather)
                        }
                    }

                    /**
                     * Invoked when a network exception occurred talking to the server or when an unexpected
                     * exception occurred creating the request or processing the response.
                     */
                    override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                        context?.toast(getString(R.string.could_not_load_city_weather))
                    }
                })

            } catch (e: Exception) {
                throw Exception(TAG, e)
            }
        }
    }
}
