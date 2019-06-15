package fr.area42.weatherandroid.weather

data class Weather (val description: String,
                    val iconUrl: String,
                    val temperature: Float,
                    val pressure: Int,
                    val humidity: Int)