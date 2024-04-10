package ru.snowadv.presentation.model

sealed class ScreenState<out T> {
    class Success<T>(val data: T): ScreenState<T>()
    data object Loading: ScreenState<Nothing>()
    class Error(val error: Throwable? = null): ScreenState<Nothing>()
    data object Empty: ScreenState<Nothing>()


    fun getCurrentData(): T? {
        return if (this is Success) this.data else null
    }
}