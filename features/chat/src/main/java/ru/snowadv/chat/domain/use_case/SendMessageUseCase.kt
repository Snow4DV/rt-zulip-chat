package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.data.repository.StubMessageRepository
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.domain.model.Resource

internal class SendMessageUseCase(val messagesRepository: MessageRepository = StubMessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> {
        return messagesRepository.sendMessage(streamName, topicName, text)
    }
}