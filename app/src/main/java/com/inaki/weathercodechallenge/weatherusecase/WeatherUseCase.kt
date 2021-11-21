package com.inaki.weathercodechallenge.weatherusecase

import com.inaki.weathercodechallenge.model.WeatherResponse
import com.inaki.weathercodechallenge.model.errorResponse.ErrorResponse
import com.inaki.weathercodechallenge.model.errorResponse.ErrorResponseFailure
import com.inaki.weathercodechallenge.model.errorResponse.ResponseErrorType
import com.inaki.weathercodechallenge.network.Rest
import com.inaki.weathercodechallenge.utils.*

typealias WeatherUseCase = ResultUseCase<WeatherResponse>

class WeatherUseCaseImpl(
    private val city: String
) : WeatherUseCase {

    override suspend fun execute(): UseCaseResult<WeatherResponse> {
        return handleRetrofitResponse(
            { Rest.retrofitClient.retrieveCityWeather(city) },
            {
                it.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(GenericServiceFailure)
            }
        ) { response ->
            ErrorResponseFailure(
                ErrorResponse(
                    ResponseErrorType.SERVICE_ERROR,
                    response.message(),
                    response.code(),
                    response.errorBody()?.string()
                )
            )

        }
    }
}