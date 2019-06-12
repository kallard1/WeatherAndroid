package fr.area42.weatherandroid

import android.app.Application
import fr.area42.weatherandroid.city.Database

class App : Application() {

    companion object {
        lateinit var instance: App

        val database: Database by lazy {
            Database(instance)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}