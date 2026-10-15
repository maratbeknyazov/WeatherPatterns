package uz.ttpu.weatherpatterns.mvc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvc.controller.WeatherController
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.ui.WeatherScreen

class MvcActivity : ComponentActivity(), WeatherView {

    private val model = WeatherModel(WeatherModule.repository)
    private val controller = WeatherController(model, view = this)
    private var status by mutableStateOf("Pick a city")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WeatherScreen(status = status, onCitySelected = controller::onCitySelected)
            }
        }
    }

    /**
     * Why no parameters? Because render() is "the Model changed: read it and
     * redraw yourself" — the View pulls the current state from the Model it holds.
     * The Controller only announces that something changed, so the View and the
     * Controller stay decoupled: any number of Views (or a test double) can render
     * the same Model without the Controller knowing about them.
     *
     * If the Controller passed the Weather into the View instead (render(weather)),
     * the View would stop reading the Model and the Controller would have to know
     * exactly which data each View wants: every state change would require a new
     * parameter or a new method signature, the Controller would grow into the
     * god-class from Question 1, and testing the View would require a Controller.
     */
    override fun render() {
        status = when {
            model.isLoading -> "Loading…"
            model.error != null -> "Error: ${model.error}"
            else -> model.weather?.let { "${it.city}: ${it.temperatureC}°C, ${it.condition}" }
                ?: "Pick a city"
        }
    }
}
