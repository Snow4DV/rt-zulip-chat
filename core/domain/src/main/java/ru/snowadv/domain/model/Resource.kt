package ru.snowadv.domain.model

sealed class Resource<out T> {
    data object Loading: Resource<Nothing>()
    class Error<T>(val throwable: Throwable? = null): Resource<T>()
    class Success<T>(val data: T): Resource<T>()
}