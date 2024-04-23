package ru.snowadv.channels.presentation.channel_list.elm

import ru.snowadv.event_api.helper.EventInfoHolder
import ru.snowadv.event_api.model.EventSenderType

internal sealed interface ChannelListEventElm {

    sealed interface Ui : ChannelListEventElm {
        data class ChangedSearchQuery(val query: String) : Ui
        data object SearchIconClicked : Ui
    }

    sealed interface Internal : ChannelListEventElm
}