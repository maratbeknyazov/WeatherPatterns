package uz.ttpu.weatherpatterns

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.ttpu.weatherpatterns.data.api.FakeWeatherApi
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

class WeatherRepositoryTest {

    private val repository = WeatherRepository(FakeWeatherApi(latencyMs = 0))

    @Test
    fun known_city_returns_success() = runBlocking<Unit> {
        // ask for "Tashkent"; assert isSuccess and temperatureC == 24
        val result = repository.getWeather("Tashkent")
        assertTrue(result.isSuccess)
        assertEquals(24, result.getOrNull()?.temperatureC)
    }

    @Test
    fun unknown_city_returns_failure() = runBlocking<Unit> {
        // ask for "Atlantis"; assert isFailure
        val result = repository.getWeather("Atlantis")
        assertTrue(result.isFailure)
    }
}
