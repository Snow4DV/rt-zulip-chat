package ru.snowadv.people_presentation.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.people_presentation.ui.elm.PeopleListElmMapper
import ru.snowadv.people_presentation.api.PeopleScreenFactory
import ru.snowadv.people_presentation.presentation.elm.PeopleListActorElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListCommandElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListEffectElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListEventElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListReducerElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListStateElm
import ru.snowadv.people_presentation.ui.elm.PeopleListEffectUiElm
import ru.snowadv.people_presentation.ui.elm.PeopleListEventUiElm
import ru.snowadv.people_presentation.ui.elm.PeopleListStateUiElm
import ru.snowadv.people_presentation.ui.feature.PeopleScreenFactoryImpl
import ru.snowadv.presentation.elm.ElmMapper
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface PeoplePresentationModule {
    @Binds
    fun bindPeopleListActorElm(peopleActorElm: PeopleListActorElm): Actor<PeopleListCommandElm, PeopleListEventElm>
    @Binds
    fun bindPeopleListReducer(peopleListReducerElm: PeopleListReducerElm): ScreenDslReducer<PeopleListEventElm, PeopleListEventElm.Ui, PeopleListEventElm.Internal, PeopleListStateElm, PeopleListEffectElm, PeopleListCommandElm>
    @Binds
    fun bindPeopleScreenFactoryImpl(impl: PeopleScreenFactoryImpl): PeopleScreenFactory
    @Binds
    fun bindPeopleElmMapperImpl(mapper: PeopleListElmMapper): ElmMapper<PeopleListStateElm, PeopleListEffectElm, PeopleListEventElm, PeopleListStateUiElm, PeopleListEffectUiElm, PeopleListEventUiElm>
}
