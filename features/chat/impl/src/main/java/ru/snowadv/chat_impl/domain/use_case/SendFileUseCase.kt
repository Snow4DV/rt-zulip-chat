package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class SendFileUseCase @Inject constructor(private val messagesRepository: MessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
        inputStreamOpener: InputStreamOpener,
        mimeType: String?,
        extension: String?,
    ): Flow<Resource<Unit>> {
        return messagesRepository.sendFile(streamName, topicName, inputStreamOpener, mimeType, extension)
    }
}