package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.MoveMessageToOtherTopicUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveMessageUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class RemoveMessageUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    RemoveMessageUseCase {
    override operator fun invoke(messageId: Long): Flow<Resource<Unit>> {
        return messagesRepository.removeMessage(messageId = messageId)
    }
}