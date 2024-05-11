package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource

interface SendFileUseCase {
    operator fun invoke(
        streamName: String,
        topicName: String,
        inputStreamOpener: InputStreamOpener,
        mimeType: String?,
        extension: String?,
    ): Flow<Resource<Unit>>
}