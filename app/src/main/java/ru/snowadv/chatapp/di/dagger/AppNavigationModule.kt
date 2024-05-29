package ru.snowadv.chatapp.di.dagger

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.chatapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.chatapp.glue.navigation.ChatRouterImpl
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.chatapp.glue.configuration.BaseUrlProviderImpl
import ru.snowadv.chatapp.glue.navigation.AuthRouterImpl
import ru.snowadv.chatapp.glue.navigation.MessageActionsRouterImpl
import ru.snowadv.chatapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.chatapp.glue.navigation.ProfileRouterImpl
import ru.snowadv.home_presentation.local_navigation.impl.InnerHomeScreenFactoryImpl
import ru.snowadv.message_actions_presentation.navigation.MessageActionsRouter
import ru.snowadv.people_presentation.navigation.PeopleRouter
import ru.snowadv.profile_presentation.navigation.ProfileRouter
import javax.inject.Singleton

@Module
internal interface AppNavigationModule {
    @Binds
    fun bindChannelsRouterImpl(channelsRouterImpl: ChannelsRouterImpl): ChannelsRouter
    @Binds
    fun bindChatRouterImpl(chatRouterImpl: ChatRouterImpl): ChatRouter
    @Binds
    fun bindPeopleRouterImpl(peopleRouterImpl: PeopleRouterImpl): PeopleRouter
    @Binds
    fun bindProfileRouterImpl(profileRouterImpl: ProfileRouterImpl): ProfileRouter
    @Binds
    fun bindAuthRouterImpl(authRouterImpl: AuthRouterImpl): AuthRouter
    @Binds
    fun bindMessageActionsRouterImpl(impl: MessageActionsRouterImpl): MessageActionsRouter

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