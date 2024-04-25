package ru.snowadv.voiceapp.glue.util

import ru.snowadv.model.Resource
import ru.snowadv.model.map

internal fun <T, R> Resource<List<T>>.mapListContent(mapper: (T) -> R): Resource<List<R>> {
    return this.map { resContent -> resContent.map { element -> mapper(element) } }
}

internal fun <T, R> Resource<List<T>>.mapNotNullListContent(mapper: (T) -> R?): Resource<List<R>> {
    return this.map { resContent -> resContent.mapNotNull { element -> mapper(element) } }
}

internal fun <T> Resource<List<T>>.filterListContent(predicate: (T) -> Boolean): Resource<List<T>> {
    return this.map { resContent -> resContent.filter { element -> predicate(element) } }
}