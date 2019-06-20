package fr.area42.weatherandroid.gps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class GpsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, GpsFragment.newInstance())
            .commit()
    }
}