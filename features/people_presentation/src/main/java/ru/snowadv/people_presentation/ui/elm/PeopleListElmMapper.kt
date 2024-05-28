package ru.snowadv.people_presentation.ui.elm

import dagger.Reusable
import ru.snowadv.model.map
import ru.snowadv.people_presentation.presentation.elm.PeopleListEffectElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListEventElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListStateElm
import ru.snowadv.people_presentation.ui.util.PeopleMappers.toUiModel
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class PeopleListElmMapper @Inject constructor() :
    ElmMapper<PeopleListStateElm, PeopleListEffectElm, PeopleListEventElm, PeopleListStateUiElm, PeopleListEffectUiElm, PeopleListEventUiElm> {
    override fun mapState(state: PeopleListStateElm): PeopleListStateUiElm {
        return PeopleListStateUiElm(
            screenState = state.screenState.map { list -> list.map { it.toUiModel() } },
            peopleRes = state.peopleRes.map { list -> list.map { it.toUiModel() } },
            isResumed = state.isResumed,
            eventQueueData = state.eventQueueData,
            searchQuery = state.searchQuery,
        )
    }

    override fun mapEffect(effect: PeopleListEffectElm): PeopleListEffectUiElm = when(effect) {
        PeopleListEffectElm.FocusOnSearchFieldAndOpenKeyboard -> PeopleListEffectUiElm.FocusOnSearchFieldAndOpenKeyboard
    }

    override fun mapUiEvent(uiEvent: PeopleListEventUiElm): PeopleListEventElm = when(uiEvent) {
        is PeopleListEventUiElm.ChangedSearchQuery -> PeopleListEventElm.Ui.ChangedSearchQuery(query = uiEvent.query)
        is PeopleListEventUiElm.ClickedOnPerson -> PeopleListEventElm.Ui.ClickedOnPerson(userId = uiEvent.userId)
        PeopleListEventUiElm.ClickedOnRetry -> PeopleListEventElm.Ui.ClickedOnRetry
        PeopleListEventUiElm.ClickedOnSearchIcon -> PeopleListEventElm.Ui.ClickedOnSearchIcon
        PeopleListEventUiElm.Init -> PeopleListEventElm.Ui.Init
        PeopleListEventUiElm.Paused -> PeopleListEventElm.Ui.Paused
        PeopleListEventUiElm.Resumed -> PeopleListEventElm.Ui.Resumed
    }

}