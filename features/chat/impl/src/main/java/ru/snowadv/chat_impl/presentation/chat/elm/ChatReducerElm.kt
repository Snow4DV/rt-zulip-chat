package ru.snowadv.chat_impl.presentation.chat.elm

import ru.snowadv.chat_impl.presentation.model.ChatEmoji
import ru.snowadv.chat_impl.presentation.model.ChatMessage
import ru.snowadv.chat_impl.presentation.model.ChatPaginationStatus
import ru.snowadv.chat_impl.presentation.model.ChatReaction
import ru.snowadv.chat_impl.presentation.util.AdapterUtils.mapToAdapterMessagesAndDates
import ru.snowadv.chat_impl.presentation.util.AdapterUtils.mapToUiAdapterMessagesAndDates
import ru.snowadv.chat_impl.presentation.util.ChatMappers.toUiChatMessage
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.event_api.helper.StateMachineQueueHelper
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.toScreenStateListMapper
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class ChatReducerElm @Inject constructor():
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
                    screenState = ScreenState.Error(event.throwable),
                    messages = emptyList(),
                    paginationStatus = ChatPaginationStatus.None,
                    eventQueueData = null,
                )
            }

            is ChatEventElm.Internal.InitialChatLoaded -> {
                state {
                    copy(
                        screenState = event.messages.messages.toScreenStateListMapper { it.mapToUiAdapterMessagesAndDates() },
                        messages = event.messages.messages.map { it.toUiChatMessage() },
                        paginationStatus = when {
                            event.messages.foundOldest -> ChatPaginationStatus.LoadedAll
                            else -> ChatPaginationStatus.HasMore
                        },
                        eventQueueData = null,
                    )
                }
                commands {
                    commandObserve()
                }
            }

            ChatEventElm.Internal.Loading -> state {
                copy(screenState = ScreenState.Loading)
            }

            ChatEventElm.Internal.MessageSent -> state {
                copy(sendingMessage = false, messageField = "")
            }

            is ChatEventElm.Internal.MoreMessagesLoaded -> {
                val messagesList = buildList {
                    addAll(event.messages.messages.map { it.toUiChatMessage() })
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
                        screenState = messagesList.toScreenStateListMapper {
                            it.mapToAdapterMessagesAndDates()
                        },
                    )
                }
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
                    } else if (state.resumed){
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
                    event.newContent?.let {
                        updateMessage(
                            messageId = event.messageId,
                            renderedContent = event.newContent,
                            eventId = event.eventId,
                        )
                    } ?: copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                }
                commandObserve()
            }

            is ChatEventElm.Internal.ServerEvent.NewMessage -> {
                state {
                    addMessage(newMessage = event.message, eventId = event.eventId)
                }
                commandObserve()
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
                    +ChatEffectElm.ExplainNotImplemented
                }
                state {
                    copy(sendingMessage = false)
                }
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

            is ChatEventElm.Ui.AddReactionClicked -> effects {
                +ChatEffectElm.OpenReactionChooser(event.messageId)
            }

            ChatEventElm.Ui.GoBackClicked -> commands {
                +ChatCommandElm.GoBack
            }

            is ChatEventElm.Ui.GoToProfileClicked -> commands {
                +ChatCommandElm.GoToProfile(event.profileId)
            }

            is ChatEventElm.Ui.MessageFieldChanged -> state {
                copy(
                    messageField = event.text, actionButtonType = if (event.text.isEmpty()) {
                        ChatStateElm.ActionButtonType.ADD_ATTACHMENT
                    } else {
                        ChatStateElm.ActionButtonType.SEND_MESSAGE
                    },
                )
            }

            is ChatEventElm.Ui.MessageLongClicked -> effects {
                +ChatEffectElm.OpenMessageActionsChooser(event.messageId, event.userId)
            }

            ChatEventElm.Ui.PaginationLoadMore, ChatEventElm.Ui.ScrolledToTop -> {
                if (state.paginationStatus != ChatPaginationStatus.Loading) {
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
                when (state.actionButtonType) {
                    ChatStateElm.ActionButtonType.SEND_MESSAGE -> {
                        if (!state.sendingMessage) {
                            commands {
                                +ChatCommandElm.SendMessage(
                                    streamName = state.stream,
                                    topicName = state.topic,
                                    text = state.messageField
                                )
                            }
                        }
                    }

                    ChatStateElm.ActionButtonType.ADD_ATTACHMENT -> effects {
                        +ChatEffectElm.ExplainNotImplemented
                    }
                }
            }

            ChatEventElm.Ui.Init -> commands {
                +ChatCommandElm.LoadInitialMessages(state.stream, state.topic)
            }

            ChatEventElm.Ui.Paused -> state {
                copy(resumed = false)
            }
            ChatEventElm.Ui.Resumed -> {
                state {
                    copy(resumed = true)
                }
                commandObserve()
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
                    emojiString = emoji.convertedEmojiString,
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
                    emojiString = emoji.convertedEmojiString,
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
                    }).filter { it.count > 0 })
                } else {
                    message
                }
            },
            eventId = eventId
        )
    }

    private fun ChatStateElm.addMessage(newMessage: ChatMessage, eventId: Long): ChatStateElm {
        return updateMessageList(messageList = messages + newMessage, eventId = eventId)
    }

    private fun ChatStateElm.replaceMessage(newMessage: ChatMessage, eventId: Long): ChatStateElm {
        return updateMessageList(
            messageList = messages.map { if (it.id == newMessage.id) newMessage else it },
            eventId = eventId
        )
    }

    private fun ChatStateElm.updateMessage(
        messageId: Long, renderedContent: String, eventId: Long
    ): ChatStateElm {
        return updateMessageList(
            messageList = messages.map { if (it.id == messageId) it.copy(text = renderedContent) else it },
            eventId = eventId
        )
    }

    private fun ChatStateElm.removeMessage(messageId: Long, eventId: Long): ChatStateElm {
        return updateMessageList(
            messageList = messages.filter { it.id != messageId }, eventId = eventId
        )
    }

    private fun ChatStateElm.updateMessageList(
        messageList: List<ChatMessage>, eventId: Long
    ): ChatStateElm {
        // messages can only be updated after initial fetch
        if (screenState is ScreenState.Error || screenState is ScreenState.Loading) return this

        return copy(
            screenState = ScreenState.Success(messageList.mapToAdapterMessagesAndDates()),
            messages = messageList,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }


}