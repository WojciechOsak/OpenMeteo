package com.wojciechosak.openmeteo.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    ResultWrapper.GenericError(code, throwable)
                }

                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: Throwable? = null) : ResultWrapper<Nothing>()
    data object NetworkError : ResultWrapper<Nothing>()
}
