package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class SendMessageUseCaseImpl@Inject constructor(private val messagesRepository: MessageRepository) :
    SendMessageUseCase {
    override operator fun invoke(
        streamName: String,
        topicName: String,
        text: String,
    ): Flow<Resource<Unit>> {
        return messagesRepository.sendMessage(streamName, topicName, text)
    }
}