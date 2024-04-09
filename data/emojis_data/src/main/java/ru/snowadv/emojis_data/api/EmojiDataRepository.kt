package ru.snowadv.emojis_data.api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.model.Resource


interface EmojiDataRepository {
    fun getAvailableEmojis(): Flow<Resource<List<DataEmoji>>>
}