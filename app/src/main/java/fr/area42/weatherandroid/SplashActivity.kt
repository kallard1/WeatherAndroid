package fr.area42.weatherandroid

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import fr.area42.weatherandroid.city.CityActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    val PERMISSION_LOCATION_PHONE = 0
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo?.isConnected ?: false

        if (isConnected) {
            // Vérification des droits pour l'accès au GPS
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_LOCATION_PHONE
                )
            } else {
                cityActivity()
            }
        } else {
            coordinatorLayout = findViewById(R.id.coordinator_layout)

            val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.settings_not_connected), Snackbar.LENGTH_INDEFINITE)
             snackbar.setAction(getString(R.string.close)) {
                 finish()
             }
            snackbar.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_LOCATION_PHONE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cityActivity()
                }
            }
        }
    }

    private fun cityActivity() {
        val intent = Intent(this, CityActivity::class.java)
        startActivity(intent)
        finish()
    }
}