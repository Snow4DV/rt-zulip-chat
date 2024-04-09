package ru.snowadv.utils

import kotlinx.coroutines.CoroutineScope
import ru.snowadv.model.Resource

fun <T> Result<T>.toResource(): Resource<T> {
    return this.fold(
        onSuccess = {
            Resource.Success(it)
        },
        onFailure = {
            Resource.Error(it)
        },
    )
}

fun <T, E> Result<T>.foldToResource(mapper: (T) -> E): Resource<E> {
    return this.fold(
        onSuccess = {
            Resource.Success(mapper(it))
        },
        onFailure = {
            Resource.Error(it)
        },
    )
}

suspend fun <T, E> Result<T>.combineFold(
    other: Result<E>,
    onBothSuccess: suspend (T, E) -> Unit,
    onFailure: suspend (Throwable) -> Unit
) {
    if (this.isSuccess && other.isSuccess) {
        onBothSuccess(this.getOrThrow(), other.getOrThrow())
    } else {
        onFailure(this.exceptionOrNull() ?: other.exceptionOrNull() ?: Exception("Unknown exception"))
    }
}

