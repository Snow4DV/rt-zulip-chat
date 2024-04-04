package ru.snowadv.presentation.model

sealed class ScreenState<out T> {
    class Success<T>(val data: T): ScreenState<T>()
    data object Loading: ScreenState<Nothing>()
    class Error(val error: Throwable? = null): ScreenState<Nothing>()
    data object Empty: ScreenState<Nothing>()

    val isLoading get() = this is Loading

    fun getCurrentData(): T? {
        return if (this is Success) this.data else null
    }

    inline fun <reified T> ScreenState<List<T>>.filtered(predicate: (T) -> Boolean): ScreenState<List<T>> {
        return this.getCurrentData()?.filter(predicate)?.let { if (it.isNotEmpty()) Success(it) else Empty} ?: this
    }
}