package com.inaki.weathercodechallenge.weatherusecase

import com.inaki.weathercodechallenge.model.WeatherResponse
import com.inaki.weathercodechallenge.utils.Failure
import com.inaki.weathercodechallenge.utils.Result
import com.inaki.weathercodechallenge.utils.onFailure
import com.inaki.weathercodechallenge.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherResult(
    coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): CoroutineScope by coroutineScope {

    private val _currentWeather by lazy {
        MutableStateFlow<Result<WeatherResponse, Failure>>(Result.loading())
    }
    val currentCityWeather: StateFlow<Result<WeatherResponse, Failure>>
        get() = _currentWeather

    internal fun getCurrentCityWeather(city: String) {
        val currentWeatherUseCase = WeatherUseCaseImpl(city)
        _currentWeather.value = Result.loading()

        launch(ioDispatcher) {
            val currentWeatherResult = currentWeatherUseCase.execute()
            currentWeatherResult
                .onSuccess { _currentWeather.value = Result.success(it) }
                .onFailure { _currentWeather.value = Result.failure(it) }
        }
    }
}