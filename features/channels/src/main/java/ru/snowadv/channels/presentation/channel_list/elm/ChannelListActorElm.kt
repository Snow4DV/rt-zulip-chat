package ru.snowadv.channels.presentation.channel_list.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

internal class ChannelListActorElm : Actor<ChannelListCommandElm, ChannelListEventElm>() {
    override fun execute(command: ChannelListCommandElm): Flow<ChannelListEventElm> = flow {}
}