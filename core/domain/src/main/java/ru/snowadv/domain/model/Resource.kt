package ru.snowadv.domain.model

sealed class Resource<out T> {
    data object Loading: Resource<Nothing>()
    class Error<T>(val message: String? = null, val data: T? = null): Resource<T>()
    class Success<T>(val data: T): Resource<T>()
}