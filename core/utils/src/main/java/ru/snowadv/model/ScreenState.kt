package ru.snowadv.model

sealed class ScreenState<out T> {
    abstract val data: T?
    data class Success<T>(override val data: T) : ScreenState<T>()
    data class Loading<T>(override val data: T? = null) : ScreenState<T>()
    data class Error<T>(val error: Throwable, override val data: T? = null) : ScreenState<T>()
    data object Empty: ScreenState<Nothing>() {
        override val data: Nothing? = null
    }

    val isLoading get() = this is Loading

    fun getCurrentData(): T? {
        return data
    }

    fun <R> map(mapper: (T) -> R): ScreenState<R> {
        return when (this) {
            is Empty -> Empty
            is Loading -> Loading(data?.let { mapper(it) })
            is Error -> Error(error, data?.let { mapper(it) })
            is Success -> Success(mapper(data))
        }
    }
}