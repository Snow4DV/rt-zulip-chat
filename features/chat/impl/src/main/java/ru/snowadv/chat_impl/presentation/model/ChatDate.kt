package ru.snowadv.chat_impl.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem
import java.time.LocalDateTime

internal data class ChatDate(
    /*
    Date time is used to make ChatDates before different messages different to DiffUtil.
    Doing otherwise might make scroll jump to the top when paginating with the ChatDate
     */
    val dateTime: LocalDateTime
): DelegateItem {
    override val id
        get() = dateTime
}