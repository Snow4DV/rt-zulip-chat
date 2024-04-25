package ru.snowadv.presentation.model

sealed class ScreenState<out T> {
    class Success<T>(val data: T) : ScreenState<T>()
    data object Loading : ScreenState<Nothing>()
    class Error(val error: Throwable? = null) : ScreenState<Nothing>()
    data object Empty : ScreenState<Nothing>()

    val isLoading get() = this is Loading

    fun getCurrentData(): T? {
        return if (this is Success) this.data else null
    }

    fun <R> map(mapper: (T) -> R): ScreenState<R> {
        return when (this) {
            is Empty -> this
            is Loading -> this
            is Error -> this
            is Success -> ScreenState.Success(mapper(data))
        }
    }
}