package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.MoveMessageToOtherTopicUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class MoveMessageToOtherTopicUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    MoveMessageToOtherTopicUseCase {
    override operator fun invoke(
        messageId: Long,
        newTopic: String,
        notifyNewThread: Boolean,
        notifyOldThread: Boolean
    ): Flow<Resource<Unit>> {
        return messagesRepository.editMessage(
            messageId = messageId,
            newContent = null,
            newSubject = newTopic,
            notifyOldThread = notifyOldThread,
            notifyNewThread = notifyNewThread,
        )
    }
}