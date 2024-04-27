package ru.snowadv.voiceapp.di.dagger

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.voiceapp.glue.auth.AuthProviderImpl
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.voiceapp.glue.repository.ChannelsRepositoryImpl
import ru.snowadv.voiceapp.glue.repository.ChatRepositoryImpl
import ru.snowadv.voiceapp.glue.repository.PeopleRepositoryImpl
import ru.snowadv.voiceapp.glue.repository.ProfileRepositoryImpl
import javax.inject.Singleton

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
    @Binds
    fun bindAuthProvider(authProviderImpl: AuthProviderImpl): AuthProvider
    @Binds
    fun bindDispatcherProvider(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider
}