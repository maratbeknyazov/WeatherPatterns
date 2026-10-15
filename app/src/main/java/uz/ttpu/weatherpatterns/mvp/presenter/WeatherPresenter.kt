package uz.ttpu.weatherpatterns.mvp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvp.WeatherContract

/**
 * The Presenter knows its View only through WeatherContract.View, so it can be
 * unit-tested with a fake View and no emulator — the main practical gain of MVP.
 */
class WeatherPresenter(
    private val view: WeatherContract.View,
    private val repository: WeatherRepository,
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {

    fun onCitySelected(city: String) {
        // TODO 1: tell the View to show the loading state first
        view.showLoading()

        // TODO 2: ask the repository for `city` in the Presenter's own scope
        scope.launch {
            val result = repository.getWeather(city)
            // TODO 3: success -> view.showWeather(weather); failure -> view.showError(message)
            result
                .onSuccess { view.showWeather(it) }
                .onFailure { view.showError(it.message ?: "Unknown error") }
        }
    }

    /** Question 9 fix: stop the coroutine so no callback reaches a destroyed View. */
    fun destroy() {
        scope.cancel()
    }
}
