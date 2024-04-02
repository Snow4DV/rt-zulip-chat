package ru.snowadv.chat.presentation.chat.state

import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

data class ChatScreenState(
    val actionInProcess: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val messageField: String = "",
)