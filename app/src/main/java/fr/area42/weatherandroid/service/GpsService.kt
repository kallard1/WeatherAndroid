package fr.area42.weatherandroid.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import fr.area42.weatherandroid.gps.GpsActivity
import fr.area42.weatherandroid.gps.GpsFragment

class GpsService : IntentService("GpsService") {
    private var locationManager: LocationManager? = null

    companion object {
        private val TAG = GpsService::class.java.simpleName
        private lateinit var broadcastIntent: Intent

        val INTERVAL = 1000.toLong() // In milliseconds
        val DISTANCE = 10.toFloat() // In meters
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "${TAG}: Started")

        if (locationManager == null) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                INTERVAL,
                DISTANCE,
                LTRLocationListener(this)
            )
            Log.d(TAG, "NETWORK_PROVIDER")
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Network provider does not exist", e)
        }

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                INTERVAL,
                DISTANCE,
                LTRLocationListener(this)
            )
            Log.d(TAG, "GPS_PROVIDER")
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "GPS provider does not exist", e)
        }
    }

    class LTRLocationListener(val context: Context) : android.location.LocationListener {
        override fun onLocationChanged(location: Location?) {
            try {
                broadcastIntent = Intent()
                broadcastIntent.action = GpsFragment.BROADCAST_ACTION

                broadcastIntent.putExtra("lat", location?.latitude)
                broadcastIntent.putExtra("lon", location?.longitude)

                context.sendBroadcast(broadcastIntent)
                Log.d(TAG, "onLocationChanged")
            } catch (e: Exception) {
                throw Exception(TAG, e)
            }
        }

        override fun onProviderDisabled(provider: String?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }
}
