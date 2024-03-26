package ru.snowadv.chat.presentation.chat.event

internal sealed class ChatScreenFragmentEvent {
    class OpenReactionChooser(val destMessageId: Long): ChatScreenFragmentEvent()
    data object ExplainError: ChatScreenFragmentEvent()
    data object ExplainNotImplemented: ChatScreenFragmentEvent()
    data object ExplainReactionAlreadyExists: ChatScreenFragmentEvent()
    data object ScrollRecyclerToTheEnd: ChatScreenFragmentEvent()
}