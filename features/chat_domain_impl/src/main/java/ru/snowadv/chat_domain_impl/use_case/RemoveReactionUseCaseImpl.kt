package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class RemoveReactionUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    RemoveReactionUseCase {
    override operator fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messagesRepository.removeReaction(messageId, reactionName)
    }
}