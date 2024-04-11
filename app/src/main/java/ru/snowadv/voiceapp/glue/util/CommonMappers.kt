package ru.snowadv.voiceapp.glue.util

import ru.snowadv.model.Resource
import ru.snowadv.model.map

internal fun <T, R> Resource<List<T>>.mapListContent(mapper: (T) -> R): Resource<List<R>> {
    return this.map { resContent -> resContent.map { element -> mapper(element) } }
}