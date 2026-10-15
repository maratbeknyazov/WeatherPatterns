package uz.ttpu.weatherpatterns.data.repository

import kotlinx.coroutines.CancellationException
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.model.Weather

/**
 * The one Model layer shared by all four patterns (MVC, MVP, MVVM, MVI).
 *
 * TODO 1: call api.fetchWeather(city) — it suspends until the fake network answers.
 * TODO 2: return Result.success(weather) when it works.
 * TODO 3: return Result.failure(e) when it throws IllegalArgumentException.
 *
 * Note: no runCatching { } here — it would swallow CancellationException too
 * (cancelling the coroutine would look like a normal "unknown city" error),
 * so we rethrow CancellationException by hand.
 */
class WeatherRepository(private val api: WeatherApi) {

    suspend fun getWeather(city: String): Result<Weather> {
        val weather = try {
            api.fetchWeather(city)
        } catch (e: CancellationException) {
            throw e                    // never turn a cancellation into a failure
        } catch (e: IllegalArgumentException) {
            return Result.failure(e)   // TODO 3
        }
        return Result.success(weather) // TODO 2
    }
}
