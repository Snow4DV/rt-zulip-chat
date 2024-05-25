package ru.snowadv.utils

import ru.snowadv.model.Resource

fun <T> Result<T>.toResource(cachedData: T? = null): Resource<T> {
    return this.fold(
        onSuccess = {
            Resource.Success(it)
        },
        onFailure = {
            Resource.Error(it, cachedData)
        },
    )
}

fun <T, E> Result<T>.foldToResource(cachedData: E? = null, mapper: (T) -> E): Resource<E> {
    return this.fold(
        onSuccess = {
            Resource.Success(mapper(it))
        },
        onFailure = {
            Resource.Error(it, cachedData)
        },
    )
}

fun <T> Resource<T>.combineWithCache(cache: T?): Resource<T> {
    return when(this) {
        is Resource.Error -> Resource.Error(throwable, data ?: cache, error)
        is Resource.Loading -> Resource.Loading(data ?: cache)
        is Resource.Success -> Resource.Success(data)
    }
}

suspend fun <T, E, R> Result<T>.combineFold(
    other: Result<E>,
    onBothSuccess: suspend (T, E) -> R,
    onFailure: suspend (Throwable) -> R
): R {
    return if (this.isSuccess && other.isSuccess) {
        onBothSuccess(this.getOrThrow(), other.getOrThrow())
    } else {
        onFailure(this.exceptionOrNull() ?: other.exceptionOrNull() ?: Exception("Unknown exception"))
    }
}

