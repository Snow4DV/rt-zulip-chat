package ru.snowadv.chat.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem
import java.time.LocalDate

internal data class ChatDate(
    val date: LocalDate
): DelegateItem {
    override val id
        get() = date
}