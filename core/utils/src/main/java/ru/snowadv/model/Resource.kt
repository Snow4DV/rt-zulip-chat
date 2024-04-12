package ru.snowadv.model

sealed class Resource<out T> {
    data object Loading: Resource<Nothing>()
    class Error(val throwable: Throwable? = null): Resource<Nothing>()
    class Success<T>(val data: T): Resource<T>()

    fun getDataOrNull(): T? = if (this is Success) this.data else null
}

fun <T1, T2> Resource<T1>.combine(other: Resource<T2>): Resource<Pair<T1, T2>> {
    return when {
        this is Resource.Error -> this
        other is Resource.Error -> other
        this is Resource.Loading || other is Resource.Loading -> Resource.Loading
        this is Resource.Success && other is Resource.Success -> {
            Resource.Success(this.data to other.data)
        }
        else -> error("Unimplemented merge strategy")
    }
}

fun <T, R> Resource<T>.map(mapper: (T) -> R): Resource<R> {
    return when(this) {
        is Resource.Success -> Resource.Success(mapper(this.data))
        is Resource.Loading -> this
        is Resource.Error -> this
    }
}

fun Resource<*>.toUnitResource(): Resource<Unit> {
    return when(this) {
        is Resource.Success -> Resource.Success(Unit)
        is Resource.Loading -> this
        is Resource.Error -> this
    }
}