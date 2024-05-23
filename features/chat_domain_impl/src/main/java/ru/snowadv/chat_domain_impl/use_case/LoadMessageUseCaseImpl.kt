package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class LoadMessageUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    LoadMessageUseCase {
    override operator fun invoke(
        messageId: Long,
        streamName: String,
    ): Flow<Resource<ChatMessage>> {
        return messagesRepository.getMessageByIdFromStream(
            messageId = messageId,
            streamName = streamName,
        )
    }
}