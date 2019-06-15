package fr.area42.weatherandroid.utils

sealed class Convert {
    object ToCelsius : Convert()
    object ToFahrenheit : Convert()
}

fun execute(value: Float, convert: Convert) = when(convert) {
    is Convert.ToCelsius -> value - 273.15F
    is Convert.ToFahrenheit -> value * 9/5 - 459.67F
}