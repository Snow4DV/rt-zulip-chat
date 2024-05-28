package ru.snowadv.profile_presentation.presentation.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ProfileStoreFactoryElm @Inject constructor(
    private val actor: Actor<ProfileCommandElm, ProfileEventElm>,
    private val reducerElm: Provider<ScreenDslReducer<ProfileEventElm, ProfileEventElm.Ui, ProfileEventElm.Internal, ProfileStateElm, ProfileEffectElm, ProfileCommandElm>>,
) {

    fun create(profileId: Long?): Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm> {
        return ElmStore(
            initialState = ProfileStateElm(profileId = profileId, eventQueueData = null, isResumed = false),
            actor = actor,
            reducer = reducerElm.get(),
        )
    }
}