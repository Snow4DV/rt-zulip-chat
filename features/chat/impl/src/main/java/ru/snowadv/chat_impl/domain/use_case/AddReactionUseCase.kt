package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_api.domain.repository.MessageRepository
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class AddReactionUseCase @Inject constructor(private val messagesRepository: MessageRepository) {
    operator fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messagesRepository.addReaction(messageId, reactionName)
    }
}