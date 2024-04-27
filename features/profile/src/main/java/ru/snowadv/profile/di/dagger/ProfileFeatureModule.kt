package ru.snowadv.profile.di.dagger

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.profile.domain.use_case.GetProfileUseCase
import ru.snowadv.profile.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.profile.presentation.profile.elm.ProfileActorElm
import ru.snowadv.profile.presentation.profile.elm.ProfileReducerElm

@Module
internal class ProfileFeatureModule {
    @Provides
    @Reusable
    fun provideProfileActorElm(router: ProfileRouter,
                               getProfileUseCase: GetProfileUseCase,
                               listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
    ): ProfileActorElm {
        return ProfileActorElm(router, getProfileUseCase, listenToPresenceEventsUseCase)
    }

    @Provides
    @Reusable
    fun provideProfileUseCase(repo: ProfileRepository): GetProfileUseCase {
        return GetProfileUseCase(repo)
    }

    @Provides
    @Reusable
    fun provideListenToPresenceEventsUseCase(eventRepository: EventRepository): ListenToPresenceEventsUseCase {
        return ListenToPresenceEventsUseCase(eventRepository)
    }

    @Provides
    fun provideReducerElm(): ProfileReducerElm {
        return ProfileReducerElm()
    }
}