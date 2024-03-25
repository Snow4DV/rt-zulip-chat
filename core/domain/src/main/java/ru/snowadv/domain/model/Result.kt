package ru.snowadv.domain.model

sealed class Result<T> {
    class Loading<T>(data: T? = null): Result<T>()
    class Error<T>(val message: String? = null, data: T? = null): Result<T>()
    class Success<T>(data: T): Result<T>()
}