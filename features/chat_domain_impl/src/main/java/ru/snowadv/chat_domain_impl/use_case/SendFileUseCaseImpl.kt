package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class SendFileUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    SendFileUseCase {
    override operator fun invoke(
        streamName: String,
        topicName: String,
        inputStreamOpener: InputStreamOpener,
        mimeType: String?,
        extension: String?,
    ): Flow<Resource<Unit>> {
        return messagesRepository.sendFile(streamName, topicName, inputStreamOpener, mimeType, extension)
    }
}