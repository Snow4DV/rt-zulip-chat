package ru.snowadv.channels_impl.presentation.channel_list.elm

sealed interface ChannelListEventElm {

    sealed interface Ui : ChannelListEventElm {
        data class ChangedSearchQuery(val query: String) : Ui
        data object SearchIconClicked : Ui
    }

    sealed interface Internal : ChannelListEventElm
}