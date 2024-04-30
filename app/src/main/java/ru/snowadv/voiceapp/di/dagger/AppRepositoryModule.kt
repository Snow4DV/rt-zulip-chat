package ru.snowadv.voiceapp.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_impl.domain.repository.ChannelsRepositoryImpl
import ru.snowadv.chat_impl.domain.repository.ChatRepositoryImpl
import ru.snowadv.people_impl.domain.repository.PeopleRepositoryImpl
import ru.snowadv.profile_impl.domain.repository.ProfileRepositoryImpl

@Module
interface AppRepositoryModule {
    @Binds
    fun bindStreamRepositoryImpl(channelsRepositoryImpl: ChannelsRepositoryImpl): ru.snowadv.channels_api.domain.repository.StreamRepository
    @Binds
    fun bindTopicRepositoryImpl(channelsRepositoryImpl: ChannelsRepositoryImpl): ru.snowadv.channels_api.domain.repository.TopicRepository
    @Binds
    fun bindMessageRepositoryImpl(chatRepositoryImpl: ChatRepositoryImpl): ru.snowadv.chat_api.domain.repository.MessageRepository
    @Binds
    fun bindEmojiRepositoryImpl(chatRepositoryImpl: ChatRepositoryImpl): ru.snowadv.chat_api.domain.repository.EmojiRepository
    @Binds
    fun bindPeopleRepositoryImpl(peopleRepositoryImpl: PeopleRepositoryImpl): ru.snowadv.people_api.domain.repository.PeopleRepository
    @Binds
    fun bindProfileRepositoryImpl(profileRepositoryImpl: ProfileRepositoryImpl): ru.snowadv.profile_api.domain.repository.ProfileRepository
}