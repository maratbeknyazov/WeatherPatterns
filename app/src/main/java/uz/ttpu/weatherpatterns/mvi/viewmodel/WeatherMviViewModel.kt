package uz.ttpu.weatherpatterns.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvi.model.WeatherIntent
import uz.ttpu.weatherpatterns.mvi.model.WeatherState

/**
 * Unidirectional data flow: Intents in, one immutable State out.
 * The ViewModel owns the state; the View only renders it.
 */
class WeatherMviViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun onIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.SelectCity -> load(intent.city)
            WeatherIntent.DismissError -> {
                // TODO 3: a new state without the error (the old weather is kept)
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun load(city: String) {
        // TODO 1: publish isLoading = true, drop any stale error
        _state.update { it.copy(isLoading = true, error = null) }

        // TODO 2: ask the repository, then publish a NEW state with
        //         isLoading = false and either weather or error
        viewModelScope.launch {
            repository.getWeather(city)
                .onSuccess { weather ->
                    _state.update { it.copy(isLoading = false, weather = weather, error = null) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Unknown error")
                    }
                }
        }
    }
}
