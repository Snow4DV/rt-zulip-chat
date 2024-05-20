package ru.snowadv.profile_presentation.ui.elm

import dagger.Reusable
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.profile_presentation.presentation.elm.ProfileEffectElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileEventElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileStateElm
import ru.snowadv.profile_presentation.ui.util.ProfileMappers.toUiModel
import javax.inject.Inject

@Reusable
internal class ProfileElmMapper @Inject constructor() :
    ElmMapper<ProfileStateElm, ProfileEffectElm, ProfileEventElm, ProfileStateUiElm, ProfileEffectUiElm, ProfileEventUiElm> {
    override fun mapState(state: ProfileStateElm): ProfileStateUiElm {
        return ProfileStateUiElm(
            screenState = state.screenState.map { it.toUiModel() },
            profileId = state.profileId,
            eventQueueData = state.eventQueueData,
            isResumed = state.isResumed,
        )
    }

    override fun mapEffect(effect: ProfileEffectElm): ProfileEffectUiElm {
        error("Instance of ProfileEffectUiElm can't be created")
    }

    override fun mapUiEvent(uiEvent: ProfileEventUiElm): ProfileEventElm = when(uiEvent) {
        ProfileEventUiElm.ClickedOnBack -> ProfileEventElm.Ui.ClickedOnBack
        ProfileEventUiElm.ClickedOnLogout -> ProfileEventElm.Ui.ClickedOnLogout
        ProfileEventUiElm.ClickedOnRetry -> ProfileEventElm.Ui.ClickedOnRetry
        ProfileEventUiElm.Init -> ProfileEventElm.Ui.Init
        ProfileEventUiElm.Paused -> ProfileEventElm.Ui.Paused
        ProfileEventUiElm.Resumed -> ProfileEventElm.Ui.Resumed
    }


}