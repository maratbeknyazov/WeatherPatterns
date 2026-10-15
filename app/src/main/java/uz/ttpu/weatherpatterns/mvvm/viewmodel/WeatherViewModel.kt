package uz.ttpu.weatherpatterns.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

/**
 * The View observes uiState; the ViewModel never calls the View, so it holds no
 * reference to an Activity or a composable. As an Android ViewModel it survives
 * rotation together with the request already in flight.
 */
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun onCitySelected(city: String) {
        // TODO 1: publish WeatherUiState.Loading
        _uiState.value = WeatherUiState.Loading

        // TODO 2: ask the repository, then publish Success or Error
        viewModelScope.launch {
            repository.getWeather(city)
                .onSuccess { _uiState.value = WeatherUiState.Success(it) }
                .onFailure {
                    _uiState.value = WeatherUiState.Error(it.message ?: "Unknown error")
                }
        }
    }
}
