package ru.snowadv.channels_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_api.domain.repository.StreamRepository
import ru.snowadv.channels_api.domain.repository.TopicRepository
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.channels_impl.domain.repository.ChannelsRepositoryImpl
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListActorElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListCommandElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListReducerElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListStateElm
import ru.snowadv.channels_impl.presentation.feature.ChannelsScreenFactoryImpl
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListActorElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListCommandElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListEffectElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListEventElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListReducerElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ChannelsFeatureModule {
    @Binds
    fun bindChannelsScreenFactoryImpl(channelsScreenFactoryImpl: ChannelsScreenFactoryImpl): ChannelsScreenFactory
    @Binds
    fun bindChannelsActorElm(channelsActorElm: ChannelListActorElm): Actor<ChannelListCommandElm, ChannelListEventElm>
    @Binds
    fun bindChannelsReducerElm(channelsReducerElm: ChannelListReducerElm): ScreenDslReducer<ChannelListEventElm, ChannelListEventElm.Ui, ChannelListEventElm.Internal, ChannelListStateElm, ChannelListEffectElm, ChannelListCommandElm>
    @Binds
    fun bindStreamListActorElm(streamListActorElm: StreamListActorElm): Actor<StreamListCommandElm, StreamListEventElm>
    @Binds
    fun bindStreamListReducerElm(streamListReducerElm: StreamListReducerElm): ScreenDslReducer<StreamListEventElm, StreamListEventElm.Ui, StreamListEventElm.Internal, StreamListStateElm, StreamListEffectElm, StreamListCommandElm>
    @Binds
    fun bindChannelsRepoImplToStreamRepo(channelsRepositoryImpl: ChannelsRepositoryImpl): StreamRepository
    @Binds
    fun bindChannelsRepoImplToTopicRepo(channelsRepositoryImpl: ChannelsRepositoryImpl): TopicRepository
}