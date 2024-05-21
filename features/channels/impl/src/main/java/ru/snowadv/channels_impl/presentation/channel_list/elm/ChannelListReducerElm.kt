package ru.snowadv.channels_impl.presentation.channel_list.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class ChannelListReducerElm @Inject constructor():
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