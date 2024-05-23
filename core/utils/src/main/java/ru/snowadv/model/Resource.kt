package ru.snowadv.model

sealed class Resource<out T> {
    abstract val data: T?
    data class Loading<T>(override val data: T? = null): Resource<T>()
    data class Error<T>(val throwable: Throwable, override val data: T? = null, val errorCode: String? = null): Resource<T>()
    class Success<T>(override val data: T): Resource<T>()

    fun getDataOrNull(): T? = data
}

fun <T, R> Resource<T>.map(mapper: (T) -> R): Resource<R> {
    return when(this) {
        is Resource.Success -> Resource.Success(mapper(this.data))
        is Resource.Loading -> Resource.Loading(this.data?.let { mapper(it) })
        is Resource.Error -> Resource.Error(throwable, this.data?.let { mapper(it) })
    }
}





fun Resource<*>.toUnitResource(): Resource<Unit> {
    return when(this) {
        is Resource.Success -> Resource.Success(Unit)
        is Resource.Loading -> Resource.Loading(Unit)
        is Resource.Error -> Resource.Error(throwable,)
    }
}