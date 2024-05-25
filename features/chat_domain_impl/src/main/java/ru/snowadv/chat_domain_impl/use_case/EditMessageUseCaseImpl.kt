package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class EditMessageUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    EditMessageUseCase {
    override operator fun invoke(messageId: Long, newContent: String): Flow<Resource<Unit>> {
        return messagesRepository.editMessage(
            messageId = messageId,
            newContent = newContent,
            newSubject = null,
        )
    }
}