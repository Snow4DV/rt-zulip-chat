package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_domain_api.model.ChatReaction
import ru.snowadv.events_api.helper.StateMachineQueueHelper
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.util.toScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class ChatReducerElm @Inject constructor() :
    ScreenDslReducer<ChatEventElm, ChatEventElm.Ui, ChatEventElm.Internal, ChatStateElm, ChatEffectElm, ChatCommandElm>(
        uiEventClass = ChatEventElm.Ui::class,
        internalEventClass = ChatEventElm.Internal::class,
    ) {
    override fun Result.internal(event: ChatEventElm.Internal) {
        if (!StateMachineQueueHelper.determineIfEventIsByServerAndBelongsToStateOrOther(
                queueProps = state.eventQueueData, event = event
            )
        ) return
        when (event) {
            ChatEventElm.Internal.ChangingReaction -> state {
                copy(changingReaction = true)
            }

            is ChatEventElm.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error(
                        event.throwable,
                        event.cachedMessages?.messages,
                    ),
                    messages = emptyList(),
                    paginationStatus = ChatPaginationStatus.None,
                    eventQueueData = null,
                )
            }

            is ChatEventElm.Internal.InitialChatLoaded -> {
                state {
                    copy(
                        screenState = event.messages.messages.toScreenState(),
                        messages = event.messages.messages,
                        paginationStatus = when {
                            event.messages.foundOldest -> ChatPaginationStatus.LoadedAll
                            else -> ChatPaginationStatus.HasMore
                        },
                        eventQueueData = null,
                        sendTopic = sendTopic.ifEmpty {
                            event.messages.messages.lastOrNull()?.topic ?: ""
                        },
                    )
                }
                commandObserve()
            }

            ChatEventElm.Internal.Loading -> state {
                copy(screenState = ScreenState.Loading())
            }

            is ChatEventElm.Internal.MessageSent -> {
                if (event.destTopic != state.topic && state.topic != null) {
                    state {
                        copy(
                            sendingMessage = false,
                            messageField = "",
                            topic = event.destTopic,
                            sendTopic = event.destTopic,
                        )
                    }
                    effects {
                        +ChatEffectElm.ShowTopicChangedBecauseNewMessageIsUnreachable
                    }
                    commands {
                        +ChatCommandElm.LoadInitialMessages(state.stream, event.destTopic)
                    }
                } else {
                    state {
                        copy(
                            sendingMessage = false,
                            messageField = "",
                        )
                    }
                }
            }

            is ChatEventElm.Internal.MoreMessagesLoaded -> {
                if (state.screenState.data != null) {
                    val messagesList = buildList {
                        addAll(event.messages.messages)
                        addAll(state.messages)
                    }
                    state {
                        copy(
                            paginationStatus = if (event.messages.foundOldest) {
                                ChatPaginationStatus.LoadedAll
                            } else {
                                ChatPaginationStatus.HasMore
                            },
                            messages = messagesList,
                            screenState = messagesList.toScreenState(),
                        )
                    }
                }
                markMessagesInStateAsReadCommand()
            }

            ChatEventElm.Internal.ReactionChanged -> state {
                copy(changingReaction = false)
            }

            ChatEventElm.Internal.SendingMessage -> state {
                copy(sendingMessage = true)
            }

            is ChatEventElm.Internal.ServerEvent.EventQueueFailed -> {
                commands {
                    if (event.recreateQueue) {
                        +ChatCommandElm.LoadInitialMessages(state.stream, state.topic)
                    } else if (state.resumed) {
                        +ChatCommandElm.ObserveEvents(
                            streamName = state.stream,
                            topicName = state.topic,
                            isRestart = true,
                            queueProps = state.eventQueueData,
                        )
                    }
                }
            }

            is ChatEventElm.Internal.ServerEvent.EventQueueRegistered -> {
                state {
                    copy(
                        eventQueueData = EventQueueProperties(
                            event.queueId, event.timeoutSeconds, event.eventId
                        ),
                    )
                }
                commandObserve()
                markMessagesInStateAsReadCommand()
            }

            is ChatEventElm.Internal.ServerEvent.EventQueueUpdated -> {
                updateStateAfterEvent(event)
                commandObserve()
            }

            is ChatEventElm.Internal.ServerEvent.MessageDeleted -> {
                state {
                    removeMessage(event.messageId, event.eventId)
                }
                commandObserve()
            }

            is ChatEventElm.Internal.ServerEvent.MessageUpdated -> {
                state {
                    updateMessage(
                        messageId = event.messageId,
                        renderedContent = event.newContent,
                        eventId = event.eventId,
                        newSubject = event.newSubject,
                    )
                }
                if (state.topic != null && state.topic == event.newSubject && event.queueId != null) {
                    commands {
                        +ChatCommandElm.LoadMovedMessage(
                            event.messageId,
                            state.stream,
                            event.queueId,
                            event.eventId
                        )
                    }
                }
                commandObserve()
                markMessageAsReadCommand(event.messageId)
            }

            is ChatEventElm.Internal.ServerEvent.NewMessage -> {
                state {
                    addMessage(newMessage = event.message, eventId = event.eventId)
                }
                commandObserve()
                markMessageAsReadCommand(event.message.id)
            }

            is ChatEventElm.Internal.ServerEvent.ReactionAdded -> {
                state {
                    addReaction(
                        emoji = event.emoji,
                        messageId = event.messageId,
                        isByCurrentUser = event.currentUserReaction,
                        eventId = event.eventId,
                    )
                }
                commandObserve()
            }

            is ChatEventElm.Internal.ServerEvent.ReactionRemoved -> {
                state {
                    removeReaction(
                        emoji = event.emoji,
                        messageId = event.messageId,
                        isByCurrentUser = event.currentUserReaction,
                        eventId = event.eventId,
                    )
                }
                commandObserve()
            }

            ChatEventElm.Internal.PaginationError -> state {
                copy(
                    paginationStatus = ChatPaginationStatus.Error,
                )
            }

            ChatEventElm.Internal.PaginationLoading -> state {
                copy(
                    paginationStatus = ChatPaginationStatus.Loading,
                )
            }

            is ChatEventElm.Internal.ChangingReactionError -> {
                effects {
                    +ChatEffectElm.ShowActionErrorWithRetry(event.retryEvent)
                }
                state {
                    copy(changingReaction = false)
                }
            }

            is ChatEventElm.Internal.SendingMessageError -> {
                effects {
                    +ChatEffectElm.ShowActionErrorWithRetry(ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked)
                }
                state {
                    copy(sendingMessage = false)
                }
            }

            ChatEventElm.Internal.FileUploaded -> state { copy(uploadingFile = false) }
            ChatEventElm.Internal.UploadingFile -> state { copy(uploadingFile = true) }
            is ChatEventElm.Internal.UploadingFileError -> {
                state { copy(uploadingFile = false) }
                effects {
                    +ChatEffectElm.ShowActionErrorWithRetry(event.retryEvent)
                }
            }

            is ChatEventElm.Internal.TopicsResourceChanged -> state {
                copy(
                    topics = event.topicsRes,
                )
            }

            is ChatEventElm.Internal.InitialChatLoadedFromCache -> {
                state {
                    copy(
                        screenState = event.messages.messages.toScreenState(loading = true),
                        messages = event.messages.messages,
                        paginationStatus = ChatPaginationStatus.None,
                        eventQueueData = null,
                    )
                }
            }

            is ChatEventElm.Internal.ErrorFetchingMovedMessage -> commands {
                if (event.queueId == state.eventQueueData?.queueId) {
                    +ChatCommandElm.LoadInitialMessages(state.stream, state.topic)
                }
            }

            is ChatEventElm.Internal.LoadedMovedMessage -> {
                state {
                    addMessage(event.message, event.eventId)
                }
                markMessageAsReadCommand(event.message.id)
            }

            is ChatEventElm.Internal.ServerEvent.MessagesRead -> {
                state {
                    changeIfMessagesAreRead(
                        messagesIds = event.addFlagMessagesIds.toSet(),
                        eventId = event.eventId,
                        newState = true,
                    )
                }
                commandObserve()
            }
            is ChatEventElm.Internal.ServerEvent.MessagesUnread -> {
                state {
                    changeIfMessagesAreRead(
                        messagesIds = event.removeFlagMessagesIds.toSet(),
                        eventId = event.eventId,
                        newState = false,
                    )
                }
                commandObserve()
            }
        }
    }

    override fun Result.ui(event: ChatEventElm.Ui) {
        when (event) {
            is ChatEventElm.Ui.AddChosenReaction -> commands {
                if (!state.changingReaction) {
                    +ChatCommandElm.AddChosenReaction(event.messageId, event.reactionName)
                }
            }

            is ChatEventElm.Ui.AddReactionClicked -> if (!state.changingReaction) {
                effects {
                    +ChatEffectElm.OpenReactionChooser(
                        event.messageId,
                        state.messages.getReactionCodesByCurrentUserIfMessageExists(event.messageId)
                    )
                }
            }

            ChatEventElm.Ui.GoBackClicked -> commands {
                +ChatCommandElm.GoBack
            }

            is ChatEventElm.Ui.MessageFieldChanged -> state {
                copy(
                    messageField = event.text,
                    actionButtonType = if (event.text.isEmpty()) {
                        ChatStateElm.ActionButtonType.ADD_ATTACHMENT
                    } else {
                        ChatStateElm.ActionButtonType.SEND_MESSAGE
                    },
                )
            }

            is ChatEventElm.Ui.MessageLongClicked -> effects {
                +ChatEffectElm.OpenMessageActionsChooser(
                    messageId = event.messageId,
                    userId = event.userId,
                    streamName = state.stream,
                    isOwner = state.messages.firstOrNull { it.id == event.messageId }?.owner
                        ?: false,
                )
            }

            ChatEventElm.Ui.PaginationLoadMore, ChatEventElm.Ui.ScrolledToNTopMessages -> {
                if (state.paginationStatus == ChatPaginationStatus.HasMore) {
                    state {
                        copy(paginationStatus = ChatPaginationStatus.Loading)
                    }
                    commands {
                        +ChatCommandElm.LoadMoreMessages(
                            streamName = state.stream,
                            topicName = state.topic,
                            firstLoadedMessageId = state.firstLoadedMessageId,
                            includeAnchor = state.firstLoadedMessageId == null,
                        )
                    }
                }
            }

            ChatEventElm.Ui.ReloadClicked -> commands {
                +ChatCommandElm.LoadInitialMessages(
                    streamName = state.stream,
                    topicName = state.topic,
                )
            }

            is ChatEventElm.Ui.RemoveReaction -> commands {
                if (!state.changingReaction) {
                    +ChatCommandElm.RemoveReaction(
                        messageId = event.messageId,
                        reactionName = event.reactionName,
                    )
                }
            }

            ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked -> {
                if (state.sendTopic.isBlank()) {
                    commands {
                        +ChatCommandElm.LoadTopicsFromCurrentStream(state.streamId)
                    }
                    state {
                        copy(isTopicChooserVisible = true, isTopicEmptyErrorVisible = true)
                    }
                    return
                }
                when (state.actionButtonType) {
                    ChatStateElm.ActionButtonType.SEND_MESSAGE -> {
                        if (!state.sendingMessage) {
                            commands {
                                +ChatCommandElm.SendMessage(
                                    streamName = state.stream,
                                    topicName = state.sendTopic,
                                    text = state.messageField
                                )
                            }
                        }
                    }

                    ChatStateElm.ActionButtonType.ADD_ATTACHMENT -> effects {
                        +ChatEffectElm.OpenFileChooser
                    }
                }
            }

            ChatEventElm.Ui.Init -> commands {
                +ChatCommandElm.LoadInitialMessages(state.stream, state.topic)
                +ChatCommandElm.LoadTopicsFromCurrentStream(state.streamId)
            }

            ChatEventElm.Ui.Paused -> {
                state {
                    copy(resumed = false)
                }
                commands {
                    +ChatCommandElm.CancelObservation
                }
            }

            ChatEventElm.Ui.Resumed -> {
                state {
                    copy(resumed = true)
                }
                commandObserve()
            }

            ChatEventElm.Ui.FileChoosingDismissed -> Unit

            is ChatEventElm.Ui.FileWasChosen -> commands {
                +ChatCommandElm.AddAttachment(
                    state.stream,
                    state.sendTopic,
                    event.mimeType,
                    event.inputStreamOpener,
                    event.extension,
                )
            }

            is ChatEventElm.Ui.ClickedOnTopic -> {
                state {
                    copy(
                        eventQueueData = null,
                        topic = event.topicName,
                        sendTopic = event.topicName,
                        isTopicChooserVisible = false,
                    )
                }
                commands {
                    +ChatCommandElm.LoadInitialMessages(state.stream, state.topic)
                }
            }

            ChatEventElm.Ui.ClickedOnLeaveTopic -> {
                state {
                    copy(
                        eventQueueData = null,
                        topic = null,
                        isTopicChooserVisible = true,
                    )
                }
                commands {
                    +ChatCommandElm.LoadInitialMessages(state.stream, null)
                }
            }

            is ChatEventElm.Ui.TopicChanged -> state {
                copy(sendTopic = event.newTopic, isTopicEmptyErrorVisible = false)
            }

            is ChatEventElm.Ui.EditMessageClicked -> effects {
                +ChatEffectElm.OpenMessageEditor(
                    messageId = event.messageId,
                    streamName = state.stream
                )
            }
            is ChatEventElm.Ui.MoveMessageClicked -> effects {
                state.messages.firstOrNull { it.id == event.messageId }?.let { message ->
                    +ChatEffectElm.OpenMessageTopicChanger(
                        messageId = event.messageId,
                        streamId = state.streamId,
                        topicName = message.topic,
                    )
                }
            }

            is ChatEventElm.Ui.MessageMovedToNewTopic -> {
                if (event.topicName != state.topic && state.topic != null) {
                    state {
                        copy(
                            topic = event.topicName,
                            sendTopic = event.topicName,
                        )
                    }
                    commands {
                        +ChatCommandElm.LoadInitialMessages(state.stream, event.topicName)
                    }
                }
            }

            is ChatEventElm.Ui.ReloadMessageClicked -> effects {
                +ChatEffectElm.RefreshMessageWithId(event.messageId)
            }
        }
    }

    private fun Result.updateStateAfterEvent(event: ChatEventElm.Internal.ServerEvent) {
        state {
            copy(
                eventQueueData = eventQueueData?.copy(lastEventId = event.eventId),
            )
        }
    }

    private fun Result.commandObserve() {
        if (!state.resumed || state.screenState !is ScreenState.Success) return
        commands {
            +ChatCommandElm.ObserveEvents(
                streamName = state.stream,
                topicName = state.topic,
                isRestart = false,
                queueProps = state.eventQueueData,
            )
        }
    }

    private fun ChatStateElm.addReaction(
        emoji: ChatEmoji,
        messageId: Long,
        isByCurrentUser: Boolean,
        eventId: Long,
    ): ChatStateElm {
        val oldReaction =
            messages.firstOrNull { it.id == messageId }?.reactions?.firstOrNull { it.emojiCode == emoji.code }
                ?: ChatReaction(
                    name = emoji.name,
                    emojiCode = emoji.code,
                    count = 0,
                    userReacted = isByCurrentUser,
                )
        return updateOrAddReactionToMessage(
            newReaction = oldReaction.copy(
                count = oldReaction.count + 1,
                userReacted = oldReaction.userReacted || isByCurrentUser
            ),
            messageId = messageId,
            eventId = eventId,
        )
    }

    private fun ChatStateElm.removeReaction(
        emoji: ChatEmoji,
        messageId: Long,
        isByCurrentUser: Boolean,
        eventId: Long,
    ): ChatStateElm {
        val oldReaction =
            messages.firstOrNull { it.id == messageId }?.reactions?.firstOrNull { it.emojiCode == emoji.code }
                ?: ChatReaction(
                    name = emoji.name,
                    emojiCode = emoji.code,
                    count = 0,
                    userReacted = isByCurrentUser,
                )

        return updateOrAddReactionToMessage(
            newReaction = oldReaction.copy(
                count = oldReaction.count - 1,
                userReacted = oldReaction.userReacted && !isByCurrentUser
            ),
            messageId = messageId,
            eventId = eventId,
        )
    }

    private fun ChatStateElm.updateOrAddReactionToMessage(
        newReaction: ChatReaction,
        messageId: Long,
        eventId: Long,
    ): ChatStateElm {
        return updateMessageList(
            messageList = messages.map { message ->
                if (message.id == messageId) {
                    val reactionExists: Boolean =
                        message.reactions.any { it.emojiCode == newReaction.emojiCode }
                    message.copy(reactions = (if (reactionExists) {
                        message.reactions.map { reaction ->
                            if (reaction.emojiCode == newReaction.emojiCode) {
                                newReaction
                            } else {
                                reaction
                            }
                        }
                    } else {
                        message.reactions + newReaction
                    }).filter { it.count > 0 }.sortedWith(compareBy({ -it.count }, { it.name }))
                    )
                } else {
                    message
                }
            },
            eventId = eventId
        )
    }

    private fun ChatStateElm.addMessage(newMessage: ChatMessage, eventId: Long): ChatStateElm {
        return updateMessageList(messageList = (messages + newMessage).distinctBy { it.id }
            .sortedBy { it.sentAt }, eventId = eventId)
    }

    private fun ChatStateElm.replaceMessage(newMessage: ChatMessage, eventId: Long): ChatStateElm {
        return updateMessageList(
            messageList = messages.map { if (it.id == newMessage.id) newMessage else it },
            eventId = eventId
        )
    }

    private fun ChatStateElm.updateMessage(
        messageId: Long, renderedContent: String?, newSubject: String?, eventId: Long,
    ): ChatStateElm {
        return updateMessageList(
            messageList = messages.mapNotNull { message ->
                when {
                    message.id != messageId -> message
                    newSubject != null && topic != null && newSubject != topic -> null
                    else -> message.copy(
                        topic = newSubject ?: message.topic,
                        content = renderedContent ?: message.content
                    )
                }
            },
            eventId = eventId
        )
    }

    private fun ChatStateElm.removeMessage(messageId: Long, eventId: Long): ChatStateElm {
        return updateMessageList(
            messageList = messages.filter { it.id != messageId }, eventId = eventId
        )
    }

    private fun List<ChatMessage>.getReactionCodesByCurrentUserIfMessageExists(messageId: Long): List<String> {
        return firstOrNull { it.id == messageId }?.reactions?.mapNotNull {
            if (it.userReacted) it.emojiCode else null
        } ?: emptyList()
    }

    private fun ChatStateElm.changeIfMessagesAreRead(messagesIds: Set<Long>, eventId: Long, newState: Boolean): ChatStateElm {
        return updateMessageList(
            messageList = messages.map {
                if (it.id in messagesIds) {
                    it.copy(isRead = newState)
                } else {
                    it
                }
            },
            eventId = eventId,
        )
    }

    private fun ChatStateElm.updateMessageList(
        messageList: List<ChatMessage>, eventId: Long
    ): ChatStateElm {
        // messages can only be updated after initial fetch
        if (screenState is ScreenState.Error || screenState is ScreenState.Loading) return this

        return copy(
            screenState = ScreenState.Success(messageList),
            messages = messageList,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }



    private fun Result.markMessagesInStateAsReadCommand() {
        commands {
            state.messages.map { it.id }.let { unreadMessages ->
                if (unreadMessages.isNotEmpty()) {
                    +ChatCommandElm.MarkMessagesAsRead(unreadMessages)
                }
            }

        }
    }

    private fun Result.markMessageAsReadCommand(messageId: Long) {
        commands {
            +ChatCommandElm.MarkMessagesAsRead(
                messagesIds = listOf(messageId),
            )
        }
    }
}