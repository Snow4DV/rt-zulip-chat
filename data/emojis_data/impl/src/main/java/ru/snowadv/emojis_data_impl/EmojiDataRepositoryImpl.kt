package ru.snowadv.emojis_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.emojis_data_impl.util.EmojisMapper.toDataEmoji
import ru.snowadv.emojis_data_impl.util.EmojisMapper.toDataEmojiList
import ru.snowadv.emojis_data_impl.util.EmojisMapper.toEntityEmojiList
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class EmojiDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val emojisDao: EmojisDao,
): EmojiDataRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<DataEmoji>>> = flow {
        val cachedData = emojisDao.getEmojis().map { it.toDataEmoji() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        val remoteEmojis = api.getEmojis()
        remoteEmojis.getOrNull()?.let { emojisDao.updateEmojisIfChanged(it.toEntityEmojiList()) }

        emit(api.getEmojis().foldToResource(cachedData) { it.toDataEmojiList().distinctBy { emoji -> emoji.code } })
    }.flowOn(dispatcherProvider.io)
}