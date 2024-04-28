package ru.snowadv.emojis_data_api.model

import kotlinx.coroutines.flow.Flow
import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.model.Resource


interface EmojiDataRepository {
    fun getAvailableEmojis(): Flow<Resource<List<DataEmoji>>>
}