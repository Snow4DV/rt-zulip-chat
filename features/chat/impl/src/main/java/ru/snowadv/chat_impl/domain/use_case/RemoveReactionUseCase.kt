package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class RemoveReactionUseCase @Inject constructor(private val messagesRepository: MessageRepository) {
    operator fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messagesRepository.removeReaction(messageId, reactionName)
    }
}