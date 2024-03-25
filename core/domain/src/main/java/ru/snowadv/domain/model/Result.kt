package ru.snowadv.domain.model

sealed class Result<out T> {
    data object Loading: Result<Nothing>()
    class Error<T>(val message: String? = null, data: T? = null): Result<T>()
    class Success<T>(data: T): Result<T>()
}