package ru.snowadv.emojis_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.emojis_data.util.toDataEmojiList
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.stub.StubZulipApi
import ru.snowadv.utils.foldToResource

class EmojiDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val api: ZulipApi,
): EmojiDataRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<DataEmoji>>> = flow {
        emit(Resource.Loading)
        emit(api.getEmojis().foldToResource { it.toDataEmojiList() })
    }.flowOn(ioDispatcher)
}