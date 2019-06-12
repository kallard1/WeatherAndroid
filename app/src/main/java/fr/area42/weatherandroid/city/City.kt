package fr.area42.weatherandroid.city

data class City(var id: Long,
                var name: String) {
    constructor(name: String) : this(-1, name)
}