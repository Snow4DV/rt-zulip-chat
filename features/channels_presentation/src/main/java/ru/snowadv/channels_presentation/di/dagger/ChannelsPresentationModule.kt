package ru.snowadv.channels_presentation.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListActorElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListCommandElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListReducerElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListStateElm
import ru.snowadv.channels_presentation.feature.ChannelsScreenFactoryImpl
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListActorElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListCommandElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEffectElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListReducerElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListStateElm
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListEffectUiElm
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListElmMapper
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListEventUiElm
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListStateUiElm
import ru.snowadv.presentation.elm.ElmMapper
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ChannelsPresentationModule {
    @Binds
    fun bindChannelListActorElm(channelsActorElm: ChannelListActorElm): Actor<ChannelListCommandElm, ChannelListEventElm>
    @Binds
    fun bindChanelListReducerElm(channelsReducerElm: ChannelListReducerElm): ScreenDslReducer<ChannelListEventElm, ChannelListEventElm.Ui, ChannelListEventElm.Internal, ChannelListStateElm, ChannelListEffectElm, ChannelListCommandElm>
    @Binds
    fun bindStreamListActorElm(streamListActorElm: StreamListActorElm): Actor<StreamListCommandElm, StreamListEventElm>
    @Binds
    fun bindStreamListReducerElm(streamListReducerElm: StreamListReducerElm): ScreenDslReducer<StreamListEventElm, StreamListEventElm.Ui, StreamListEventElm.Internal, StreamListStateElm, StreamListEffectElm, StreamListCommandElm>
    @Binds
    fun bindStreamListElmMapper(streamListElmMapper: StreamListElmMapper): ElmMapper<StreamListStateElm, StreamListEffectElm, StreamListEventElm, StreamListStateUiElm, StreamListEffectUiElm, StreamListEventUiElm>
    @Binds
    fun bindChannelScreenFactoryImpl(channelsScreenFactoryImpl: ChannelsScreenFactoryImpl): ChannelsScreenFactory
}
