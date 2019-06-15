package fr.area42.weatherandroid.city

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fr.area42.weatherandroid.R
import fr.area42.weatherandroid.weather.WeatherActivity
import fr.area42.weatherandroid.weather.WeatherFragment

class CityActivity : AppCompatActivity(), CityFragment.CityFragmentListener {
    private lateinit var cityFragment: CityFragment
    private var currentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_main)

        cityFragment = supportFragmentManager.findFragmentById(R.id.city_fragment) as CityFragment
        cityFragment.listener = this

    }

    override fun onCitySelected(city: City) {
        currentCity = city
        startWeatherActivity(city)
    }

    private fun startWeatherActivity(city: City) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra(WeatherFragment.EXTRA_CITY_NAME, city.name)
        startActivity(intent)
    }
}
