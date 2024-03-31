package ru.snowadv.presentation.util

import ru.snowadv.domain.model.Resource
import ru.snowadv.presentation.model.ScreenState

fun <T> Array<T>.mapInPlace(transform: (T) -> T): Array<T> {
    for (i in this.indices) {
        this[i] = transform(this[i])
    }
    return this
}

fun IntArray.mapInPlace(transform: (Int) -> Int): IntArray {
    for (i in this.indices) {
        this[i] = transform(this[i])
    }
    return this
}

fun <T, E> Resource<T>.toScreenState(mapper: (T) -> E, isEmptyChecker: ((T) -> Boolean)? = null): ScreenState<E> {
    return when(this) {
        is Resource.Error -> {
            ScreenState.Error(this.throwable)
        }
        is Resource.Loading -> {
            ScreenState.Loading
        }
        is Resource.Success -> {
            if (isEmptyChecker?.invoke(this.data) == true) {
                ScreenState.Empty
            } else {
                ScreenState.Success(mapper(this.data))
            }
        }
    }
}