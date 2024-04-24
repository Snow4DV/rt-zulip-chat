package ru.snowadv.channels.presentation.channel_list.elm

import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

internal class ChannelListReducerElm :
    ScreenDslReducer<ChannelListEventElm, ChannelListEventElm.Ui, ChannelListEventElm.Internal, ChannelListStateElm, ChannelListEffectElm, ChannelListCommandElm>(
        uiEventClass = ChannelListEventElm.Ui::class,
        internalEventClass = ChannelListEventElm.Internal::class,
    ) {
    override fun Result.internal(event: ChannelListEventElm.Internal) {}

    override fun Result.ui(event: ChannelListEventElm.Ui) {
        when (event) {
            is ChannelListEventElm.Ui.ChangedSearchQuery -> state {
                copy(searchQuery = event.query).takeIf { event.query != searchQuery } ?: this
            }
            ChannelListEventElm.Ui.SearchIconClicked -> effects {
                +ChannelListEffectElm.ShowKeyboardAndFocusOnTextField
            }
        }
    }
}