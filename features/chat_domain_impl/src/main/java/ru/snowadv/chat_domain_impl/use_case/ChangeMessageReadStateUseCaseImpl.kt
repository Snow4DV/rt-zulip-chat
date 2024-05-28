package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.ChangeMessageReadStateUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class ChangeMessageReadStateUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    ChangeMessageReadStateUseCase {
    override fun invoke(messagesIds: List<Long>, newState: Boolean): Flow<Resource<Unit>> {
        return messagesRepository.changeMessagesReadState(
            messagesIds = messagesIds,
            newState = newState,
        )
    }
}