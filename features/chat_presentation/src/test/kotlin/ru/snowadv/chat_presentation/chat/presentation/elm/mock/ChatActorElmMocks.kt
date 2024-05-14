package ru.snowadv.chat_presentation.chat.presentation.elm.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.toImmutableList
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_presentation.chat.presentation.elm.data.ChatActorElmTestData
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import ru.snowadv.test_utils.exception.DataException

internal class AddReactionUseCaseMock : AddReactionUseCase {
    override fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(Unit))
        emit(Resource.Error(DataException()))
    }
}

internal class GetCurrentMessagesUseCaseMock(
    private val data: ChatActorElmTestData,
) : GetCurrentMessagesUseCase {

    override fun invoke(
        streamName: String,
        topicName: String,
    ): Flow<Resource<ChatPaginatedMessages>> = flow {
        emit(Resource.Loading())
        emit(Resource.Loading(data.cachedMessages.toTestPaginatedMessages()))
        emit(Resource.Success(data.remoteMessages.toTestPaginatedMessages()))
        emit(Resource.Error(DataException(), data.cachedMessages.toTestPaginatedMessages()))
    }
}

internal class GetEmojisUseCaseMock(
    private val data: ChatActorElmTestData,
) : GetEmojisUseCase {
    override fun invoke(): Flow<Resource<List<ChatEmoji>>> = flow {
        emit(Resource.Loading(data.emojis))
        emit(Resource.Success(data.updatedEmojis))
        emit(Resource.Error(DataException(), data.updatedEmojis))
    }
}

internal class ListenToChatEventsUseCaseMock(
    private val data: ChatActorElmTestData,
) : ListenToChatEventsUseCase {
    override fun invoke(
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
        streamName: String,
        topicName: String,
    ): Flow<DomainEvent> = flow {
        data.events.forEach { emit(it) }
    }
}

internal class LoadMoreMessagesUseCaseMock(
    private val data: ChatActorElmTestData,
) : LoadMoreMessagesUseCase {
    override fun invoke(
        streamName: String,
        topicName: String,
        firstLoadedMessageId: Long?,
        includeAnchor: Boolean,
    ): Flow<Resource<ChatPaginatedMessages>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(data.secondPageMessages.toTestPaginatedMessages()))
        emit(Resource.Error(DataException(), data.secondPageMessages.toTestPaginatedMessages()))
    }
}

internal class RemoveReactionUseCaseMock() : RemoveReactionUseCase {
    override fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(Unit))
        emit(Resource.Error(DataException(), Unit))
    }
}

internal class SendFileUseCaseMock() : SendFileUseCase {
    override fun invoke(
        streamName: String,
        topicName: String,
        inputStreamOpener: InputStreamOpener,
        mimeType: String?,
        extension: String?,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(Unit))
        emit(Resource.Error(DataException(), Unit))
    }
}

internal class SendMessageUseCaseMock () : SendMessageUseCase {
    override fun invoke(
        streamName: String,
        topicName: String,
        text: String,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(Unit))
        emit(Resource.Error(DataException(), Unit))
    }
}

internal class ChatRouterMock : ChatRouter {
    private val _commands = mutableListOf<String>()
    val commands: List<String> = _commands
    override fun goBack() {
        _commands.add("goBack()")
    }

    override fun openProfile(profileId: Long) {
        _commands.add("openProfile($profileId)")
    }
}

private fun List<ChatMessage>.toTestPaginatedMessages(): ChatPaginatedMessages {
    return ChatPaginatedMessages(
        messages = this,
        foundAnchor = true,
        foundOldest = true,
        foundNewest = true,
    )
}
