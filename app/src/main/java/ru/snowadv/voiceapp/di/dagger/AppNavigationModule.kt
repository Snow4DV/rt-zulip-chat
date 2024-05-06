package ru.snowadv.voiceapp.di.dagger

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.snowadv.auth_api.domain.navigation.AuthRouter
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.voiceapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.voiceapp.glue.navigation.ChatRouterImpl
import ru.snowadv.home_impl.presentation.feature.InnerHomeScreenFactoryImpl
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.voiceapp.glue.navigation.AuthRouterImpl
import ru.snowadv.voiceapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.voiceapp.glue.navigation.ProfileRouterImpl
import javax.inject.Singleton

@Module
internal interface AppNavigationModule {
    @Binds
    fun bindChannelsRouterImpl(channelsRouterImpl: ChannelsRouterImpl): ChannelsRouter
    @Binds
    fun bindChatRouterImpl(chatRouterImpl: ChatRouterImpl): ChatRouter
    @Binds
    fun bindHomeScreenFactoryImpl(homeScreenFactoryImpl: InnerHomeScreenFactoryImpl): InnerHomeScreenFactory
    @Binds
    fun bindPeopleRouterImpl(peopleRouterImpl: PeopleRouterImpl): PeopleRouter
    @Binds
    fun bindProfileRouterImpl(profileRouterImpl: ProfileRouterImpl): ProfileRouter
    @Binds
    fun bindAuthRouterImpl(authRouterImpl: AuthRouterImpl): AuthRouter

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