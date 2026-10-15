package uz.ttpu.weatherpatterns.mvi.model

import uz.ttpu.weatherpatterns.data.model.Weather

/** Immutable: never modified in place, only replaced by a copy. */
data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
)

/** What the user wants to do — the only way to change the state from the View. */
sealed interface WeatherIntent {
    data class SelectCity(val city: String) : WeatherIntent
    data object DismissError : WeatherIntent
}
