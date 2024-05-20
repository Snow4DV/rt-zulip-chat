package ru.snowadv.profile_presentation.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.profile_presentation.api.ProfileScreenFactory
import ru.snowadv.profile_presentation.presentation.elm.ProfileActorElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileCommandElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileEffectElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileEventElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileReducerElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileStateElm
import ru.snowadv.profile_presentation.ui.elm.ProfileEffectUiElm
import ru.snowadv.profile_presentation.ui.elm.ProfileElmMapper
import ru.snowadv.profile_presentation.ui.elm.ProfileEventUiElm
import ru.snowadv.profile_presentation.ui.elm.ProfileStateUiElm
import ru.snowadv.profile_presentation.ui.feature.ProfileScreenFactoryImpl
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ProfilePresentationModule {
    @Binds
    fun bindProfileActorElm(profileActorElm: ProfileActorElm): Actor<ProfileCommandElm, ProfileEventElm>
    @Binds
    fun bindProfileReducerElm(profileReducerElm: ProfileReducerElm): ScreenDslReducer<ProfileEventElm, ProfileEventElm.Ui, ProfileEventElm.Internal, ProfileStateElm, ProfileEffectElm, ProfileCommandElm>
    @Binds
    fun bindProfileScreenFactoryImpl(impl: ProfileScreenFactoryImpl): ProfileScreenFactory
    @Binds
    fun bindProfileElmMapper(mapper: ProfileElmMapper): ElmMapper<ProfileStateElm, ProfileEffectElm, ProfileEventElm, ProfileStateUiElm, ProfileEffectUiElm, ProfileEventUiElm>
}
