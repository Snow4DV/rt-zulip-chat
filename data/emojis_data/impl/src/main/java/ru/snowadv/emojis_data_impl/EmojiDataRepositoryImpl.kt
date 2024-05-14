package ru.snowadv.emojis_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.emojis_data_impl.util.EmojisMapper.toDataEmojiList
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class EmojiDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
): EmojiDataRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<DataEmoji>>> = flow {
        emit(Resource.Loading)
        emit(api.getEmojis().foldToResource { it.toDataEmojiList() })
    }.flowOn(dispatcherProvider.io)
}