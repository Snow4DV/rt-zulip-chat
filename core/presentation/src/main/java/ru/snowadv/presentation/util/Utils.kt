package ru.snowadv.presentation.util

import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState

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

fun <T> Resource<List<T>>.toScreenState(): ScreenState<List<T>> {
    return when(this) {
        is Resource.Error -> if (data?.isNotEmpty() == true) {
            ScreenState.Error(throwable, data)
        } else {
            ScreenState.Error(throwable)
        }

        is Resource.Loading -> {
            if (data?.isNotEmpty() == true) {
                ScreenState.Loading(data)
            } else {
                ScreenState.Loading()
            }
        }
        is Resource.Success -> {
            if (data.isEmpty()) {
                ScreenState.Empty
            } else {
                ScreenState.Success(data)
            }
        }
    }
}

fun <T, E> List<T>.toScreenStateListMapper(mapper: (List<T>) -> List<E>): ScreenState<List<E>> {
    return if (this.isEmpty()) {
        ScreenState.Empty
    } else {
        ScreenState.Success(mapper(this))
    }
}

fun <T> List<T>.toScreenState() : ScreenState<List<T>> {
    return if (this.isEmpty()) {
        ScreenState.Empty
    } else {
        ScreenState.Success(this)
    }
}

fun <T> ScreenState<List<T>>.filterList(predicate: (T) -> Boolean ) : ScreenState<List<T>> {
    return when(this) {
        ScreenState.Empty -> this
        is ScreenState.Error -> ScreenState.Error(error, data?.filter(predicate))
        is ScreenState.Success -> {
            val filteredList = data.filter(predicate)
            if (filteredList.isEmpty()) {
                ScreenState.Empty
            } else {
                ScreenState.Success(filteredList)
            }
        }

        is ScreenState.Loading -> ScreenState.Loading(data)
    }
}