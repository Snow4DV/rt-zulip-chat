package ru.snowadv.channels_presentation.channel_list.elm

sealed interface ChannelListEventElm {

    sealed interface Ui : ChannelListEventElm {
        data object CreateNewStreamClicked : Ui
        data class ChangedSearchQuery(val query: String) : Ui
        data object SearchIconClicked : Ui
        data class NewStreamCreated(val name: String) : Ui
    }

    sealed interface Internal : ChannelListEventElm
}