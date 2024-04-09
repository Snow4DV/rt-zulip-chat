package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.model.Resource

internal class RemoveReactionUseCase(val messagesRepository: MessageRepository) {
    operator fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messagesRepository.removeReaction(messageId, reactionName)
    }
}