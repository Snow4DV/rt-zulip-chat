package ru.snowadv.domain.model

sealed class Result<T>(val data: T?) {
    class Loading<T>(data: T? = null): Result<T>(data)
    class Error<T>(val message: String? = null, data: T? = null): Result<T>(data)
    class Success<T>(data: T? = null): Result<T>(data)
}