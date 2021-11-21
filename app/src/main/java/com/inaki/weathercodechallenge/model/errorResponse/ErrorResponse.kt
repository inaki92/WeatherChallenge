package com.inaki.weathercodechallenge.model.errorResponse

import com.inaki.weathercodechallenge.utils.ServiceFailure

data class ErrorResponse(
    val errorType: ResponseErrorType,
    val exceptionMessage: String? = null,
    val httpCode: Int? = null,
    val errorBody: String? = null
)

enum class ResponseErrorType {
    NETWORK_CONNECTION,
    SERVICE_ERROR,
    EXCEPTION_ERROR
}

class ErrorResponseFailure(errorResponse: ErrorResponse) : ServiceFailure(errorResponse)