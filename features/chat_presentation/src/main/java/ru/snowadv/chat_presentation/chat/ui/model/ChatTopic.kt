package ru.snowadv.chat_presentation.chat.ui.model

import ru.snowadv.presentation.adapter.DelegateItem
import java.time.LocalDateTime

internal data class ChatTopic(
    val name: String,
    val firstMessageId: Long,
): DelegateItem {
    override val id = "${name}_$firstMessageId"
}