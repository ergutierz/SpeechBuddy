package com.ignotusvia.speechbuddy.core

import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResult<T>(open val data: T?) {
    data class Loading<T>(override val data: T? = null) : ApiResult<T>(data)

    data class Success<T>(override val data: T?) : ApiResult<T>(data)

    data class Failure<T>(override val data: T? = null, val error: Throwable) : ApiResult<T>(data)

    companion object {
        fun <T> from(
            fallbackData: T? = null,
            errorProvider: ((Throwable) -> Throwable) = { it },
            dataProvider: () -> T?
        ): ApiResult<T> {
            return try {
                Success(dataProvider())
            } catch (error: Throwable) {
                Failure(fallbackData, errorProvider(error))
            }
        }
    }
}

fun <T> Response<T>.bodyOrError(): T? {
    return if (isSuccessful) body() else throw HttpException(this)
}