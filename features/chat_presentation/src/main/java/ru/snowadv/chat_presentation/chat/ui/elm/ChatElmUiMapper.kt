package ru.snowadv.chat_presentation.chat.ui.elm

import dagger.Reusable
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils.mapToUiAdapterMessagesAndDates
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiChatMessage
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiModel
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiType
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class ChatElmUiMapper @Inject constructor() :
    ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm> {
    override fun mapState(state: ChatStateElm): ChatStateUiElm = with(state) {
        return ChatStateUiElm(
            sendingMessage = sendingMessage,
            changingReaction = changingReaction,
            uploadingFile = uploadingFile,
            stream = stream,
            topic = topic,
            screenState = screenState.map { it.mapToUiAdapterMessagesAndDates(showTopics = topic == null) },
            messages = messages.map { it.toUiChatMessage() },
            messageField = messageField,
            actionButtonType = actionButtonType.toUiType(),
            paginationStatus = paginationStatus.toUiPaginationStatus(),
            eventQueueData = eventQueueData,
            resumed = resumed,
            sendTopic = sendTopic,
            topics = topics,
            isLoading = isLoading,
            isTopicChooserVisible = isTopicChooserVisible,
            isTopicEmptyErrorVisible = isTopicEmptyErrorVisible,
        )
    }

    override fun mapEffect(effect: ChatEffectElm): ChatEffectUiElm = with(effect) {
        when(this) {
            ChatEffectElm.OpenFileChooser -> ChatEffectUiElm.OpenFileChooser
            is ChatEffectElm.OpenMessageActionsChooser -> ChatEffectUiElm.OpenMessageActionsChooser(messageId = messageId, userId = userId, streamName = streamName, isOwner =  isOwner)
            is ChatEffectElm.OpenReactionChooser -> ChatEffectUiElm.OpenReactionChooser(destMessageId, excludeEmojisCodes)
            is ChatEffectElm.ShowSnackbarWithText -> ChatEffectUiElm.ShowSnackbarWithText(text.toUiModel())
            is ChatEffectElm.ShowActionErrorWithRetry -> ChatEffectUiElm.ShowActionErrorWithRetry(retryEvent)
            ChatEffectElm.ShowTopicChangedBecauseNewMessageIsUnreachable -> ChatEffectUiElm.ShowTopicChangedBecauseNewMessageIsUnreachable
            is ChatEffectElm.OpenMessageEditor -> ChatEffectUiElm.OpenMessageEditor(messageId, streamName)
            is ChatEffectElm.OpenMessageTopicChanger -> ChatEffectUiElm.OpenMessageTopicChanger(messageId, streamId, topicName)
        }
    }

    override fun mapUiEvent(uiEvent: ChatEventUiElm): ChatEventElm = with(uiEvent) {
        return when(this) {
            is ChatEventUiElm.AddChosenReaction -> ChatEventElm.Ui.AddChosenReaction(
                messageId = messageId,
                reactionName = reactionName,
            )
            is ChatEventUiElm.AddReactionClicked -> ChatEventElm.Ui.AddReactionClicked(messageId)
            ChatEventUiElm.FileChoosingDismissed -> ChatEventElm.Ui.FileChoosingDismissed
            is ChatEventUiElm.FileWasChosen -> ChatEventElm.Ui.FileWasChosen(
                mimeType = mimeType,
                inputStreamOpener = inputStreamOpener,
                extension = extension,
            )
            ChatEventUiElm.GoBackClicked -> ChatEventElm.Ui.GoBackClicked
            ChatEventUiElm.Init -> ChatEventElm.Ui.Init
            is ChatEventUiElm.MessageFieldChanged -> ChatEventElm.Ui.MessageFieldChanged(text)
            is ChatEventUiElm.MessageLongClicked -> ChatEventElm.Ui.MessageLongClicked(
                messageId = messageId,
                userId = userId,
            )
            ChatEventUiElm.PaginationLoadMore -> ChatEventElm.Ui.PaginationLoadMore
            ChatEventUiElm.Paused -> ChatEventElm.Ui.Paused
            ChatEventUiElm.ReloadClicked -> ChatEventElm.Ui.ReloadClicked
            is ChatEventUiElm.RemoveReaction -> ChatEventElm.Ui.RemoveReaction(messageId, reactionName)
            ChatEventUiElm.Resumed -> ChatEventElm.Ui.Resumed
            ChatEventUiElm.ScrolledToNTopMessages -> ChatEventElm.Ui.ScrolledToNTopMessages
            ChatEventUiElm.SendMessageAddAttachmentButtonClicked -> ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked
            is ChatEventUiElm.ClickedOnTopic -> ChatEventElm.Ui.ClickedOnTopic(topicName)
            ChatEventUiElm.OnLeaveTopicClicked -> ChatEventElm.Ui.ClickedOnLeaveTopic
            is ChatEventUiElm.TopicChanged -> ChatEventElm.Ui.TopicChanged(newTopic)
            is ChatEventUiElm.EditMessageClicked -> ChatEventElm.Ui.EditMessageClicked(messageId)
            is ChatEventUiElm.MoveMessageClicked -> ChatEventElm.Ui.MoveMessageClicked(messageId)
            is ChatEventUiElm.MessageMovedToNewTopic -> ChatEventElm.Ui.MessageMovedToNewTopic(topicName)
        }
    }
}