package ru.snowadv.chat_presentation.chat.ui.elm

import dagger.Reusable
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils.mapToUiAdapterMessagesAndDates
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiChatMessage
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiType
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class ChatElmMapper @Inject constructor() :
    ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm> {
    override fun mapState(state: ChatStateElm): ChatStateUiElm = with(state) {
        return ChatStateUiElm(
            sendingMessage = sendingMessage,
            changingReaction = changingReaction,
            uploadingFile = uploadingFile,
            stream = stream,
            topic = topic,
            screenState = screenState.map { it.mapToUiAdapterMessagesAndDates() },
            messages = messages.map { it.toUiChatMessage() },
            messageField = messageField,
            actionButtonType = actionButtonType.toUiType(),
            paginationStatus = paginationStatus.toUiPaginationStatus(),
            eventQueueData = eventQueueData,
            resumed = resumed,
        )
    }

    override fun mapEffect(effect: ChatEffectElm): ChatEffectUiElm = with(effect) {
        when(this) {
            ChatEffectElm.OpenFileChooser -> ChatEffectUiElm.OpenFileChooser
            is ChatEffectElm.OpenMessageActionsChooser -> ChatEffectUiElm.OpenMessageActionsChooser(messageId = messageId, userId = userId)
            is ChatEffectElm.OpenReactionChooser -> ChatEffectUiElm.OpenReactionChooser(destMessageId)
            ChatEffectElm.ShowActionError -> ChatEffectUiElm.ShowActionError
            is ChatEffectElm.ShowActionErrorWithRetry -> ChatEffectUiElm.ShowActionErrorWithRetry(retryEvent)
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
            is ChatEventUiElm.GoToProfileClicked -> ChatEventElm.Ui.GoToProfileClicked(profileId)
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
        }
    }
}