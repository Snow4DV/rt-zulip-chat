package ru.snowadv.chat.presentation.chat.event

internal sealed class ChatScreenFragmentEvent {
    class OpenReactionChooser(val destMessageId: Long): ChatScreenFragmentEvent()
    data object ExplainError: ChatScreenFragmentEvent()
}