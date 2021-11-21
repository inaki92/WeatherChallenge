package com.inaki.weathercodechallenge.utils

interface ResultUseCase<T> {
    suspend fun execute(): UseCaseResult<T>
}

typealias UseCaseResult<T> = Result<T, Failure>