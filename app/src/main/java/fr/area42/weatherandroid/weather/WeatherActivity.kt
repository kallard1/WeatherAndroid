package fr.area42.weatherandroid.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("WeatherActivity", "OnCreate()")
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, WeatherFragment.newInstance())
            .commit()
    }
}
