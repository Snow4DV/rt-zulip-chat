package ru.snowadv.profile_impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_impl.domain.repository.ProfileRepository
import ru.snowadv.profile_impl.domain.use_case.GetProfileUseCase
import ru.snowadv.profile_impl.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileActorElm
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileReducerElm
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import ru.snowadv.profile_impl.data.repository.ProfileRepositoryImpl
import ru.snowadv.profile_impl.presentation.feature.ProfileScreenFactoryImpl
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileCommandElm
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileEffectElm
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileEventElm
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ProfileFeatureModule {
    @Binds
    fun bindProfileScreenFactoryElm(profileScreenFactoryImpl: ProfileScreenFactoryImpl): ProfileScreenFactory
    @Binds
    fun bindProfileActorElm(profileActorElm: ProfileActorElm): Actor<ProfileCommandElm, ProfileEventElm>
    @Binds
    fun bindProfileReducerElm(profileReducerElm: ProfileReducerElm): ScreenDslReducer<ProfileEventElm, ProfileEventElm.Ui, ProfileEventElm.Internal, ProfileStateElm, ProfileEffectElm, ProfileCommandElm>
    @Binds
    fun bindProfileRepoImpl(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository
}