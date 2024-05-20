package ru.snowadv.channels_presentation.channel_list.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class ChannelListActorElm @Inject constructor(): Actor<ChannelListCommandElm, ChannelListEventElm>() {
    override fun execute(command: ChannelListCommandElm): Flow<ChannelListEventElm> = flow {}
}