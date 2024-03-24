package ru.snowadv.domain.model

sealed class Resource<T>(val data: T?) {
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Error<T>(throwable: Throwable? = null, data: T? = null): Resource<T>(data)
    class Success<T>(data: T? = null): Resource<T>(data)
}