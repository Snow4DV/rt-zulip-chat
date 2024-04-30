package ru.snowadv.voiceapp.di.dagger

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.voiceapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.voiceapp.glue.navigation.ChatRouterImpl
import ru.snowadv.home_impl.presentation.feature.InnerHomeScreenFactoryImpl
import ru.snowadv.voiceapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.voiceapp.glue.navigation.ProfileRouterImpl
import javax.inject.Singleton

@Module
interface AppNavigationModule {
    @Reusable
    @Binds
    fun bindChannelsRouterImpl(channelsRouterImpl: ChannelsRouterImpl): ru.snowadv.channels_api.domain.navigation.ChannelsRouter
    @Reusable
    @Binds
    fun bindChatRouterImpl(chatRouterImpl: ChatRouterImpl): ru.snowadv.chat_api.domain.navigation.ChatRouter
    @Reusable
    @Binds
    fun bindHomeScreenFactoryImpl(homeScreenFactoryImpl: InnerHomeScreenFactoryImpl): InnerHomeScreenFactory
    @Reusable
    @Binds
    fun bindPeopleRouterImpl(peopleRouterImpl: PeopleRouterImpl): ru.snowadv.people_api.domain.navigation.PeopleRouter
    @Reusable
    @Binds
    fun bindProfileRouterImpl(profileRouterImpl: ProfileRouterImpl): ru.snowadv.profile_api.domain.navigation.ProfileRouter

    companion object {
        @Singleton
        @Provides
        fun provideCicerone(): Cicerone<Router> {
            return Cicerone.create()
        }

        @Provides
        fun provideRouter(cicerone: Cicerone<Router>): Router {
            return cicerone.router
        }

        @Provides
        fun provideNavigationHolder(cicerone: Cicerone<Router>): NavigatorHolder {
            return cicerone.getNavigatorHolder()
        }
    }
}