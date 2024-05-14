package ru.snowadv.voiceapp.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_impl.data.repository.ChannelsRepositoryImpl
import ru.snowadv.channels_impl.domain.repository.StreamRepository
import ru.snowadv.channels_impl.domain.repository.TopicRepository
import ru.snowadv.chat_impl.data.repository.ChatRepositoryImpl
import ru.snowadv.chat_impl.domain.repository.EmojiRepository
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.people_impl.data.repository.PeopleRepositoryImpl
import ru.snowadv.people_impl.domain.repository.PeopleRepository
import ru.snowadv.profile_impl.data.repository.ProfileRepositoryImpl
import ru.snowadv.profile_impl.domain.repository.ProfileRepository

@Module
interface AppRepositoryModule {
    @Binds
    fun bindStreamRepositoryImpl(channelsRepositoryImpl: ChannelsRepositoryImpl): StreamRepository
    @Binds
    fun bindTopicRepositoryImpl(channelsRepositoryImpl: ChannelsRepositoryImpl): TopicRepository
    @Binds
    fun bindMessageRepositoryImpl(chatRepositoryImpl: ChatRepositoryImpl): MessageRepository
    @Binds
    fun bindEmojiRepositoryImpl(chatRepositoryImpl: ChatRepositoryImpl): EmojiRepository
    @Binds
    fun bindPeopleRepositoryImpl(peopleRepositoryImpl: PeopleRepositoryImpl): PeopleRepository
    @Binds
    fun bindProfileRepositoryImpl(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository
}