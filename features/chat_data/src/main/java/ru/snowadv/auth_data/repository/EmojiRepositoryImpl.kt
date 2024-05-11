package ru.snowadv.auth_data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.auth_data.util.EmojisMapper.toDomainEmojiList
import ru.snowadv.auth_data.util.EmojisMapper.toDomainEmoji
import ru.snowadv.auth_data.util.EmojisMapper.toEntityEmojiList
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class EmojiRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val emojisDao: EmojisDao,
) : EmojiRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>> = flow {
        val cachedData = emojisDao.getEmojis().map { it.toDomainEmoji() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        val remoteEmojis = api.getEmojis()
        remoteEmojis.getOrNull()?.let { emojisDao.updateEmojisIfChanged(it.toEntityEmojiList()) }

        emit(api.getEmojis().foldToResource(cachedData) { it.toDomainEmojiList().distinctBy { emoji -> emoji.code } })
    }.flowOn(dispatcherProvider.io)

}
