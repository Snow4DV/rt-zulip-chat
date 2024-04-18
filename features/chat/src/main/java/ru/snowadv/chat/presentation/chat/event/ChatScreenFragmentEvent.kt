package ru.snowadv.chat.presentation.chat.event

internal sealed class ChatScreenFragmentEvent {
    class OpenReactionChooser(val destMessageId: Long): ChatScreenFragmentEvent()
    class ShowInternetErrorWithRetry(val retryAction: () -> Unit): ChatScreenFragmentEvent()
    data object ExplainNotImplemented: ChatScreenFragmentEvent()
    data object ExplainReactionAlreadyExists: ChatScreenFragmentEvent()
    data object ScrollRecyclerToTheEnd: ChatScreenFragmentEvent()
    class OpenMessageActionsChooser(val messageId: Long, val userId: Long): ChatScreenFragmentEvent()
}