package ru.snowadv.home_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.home_api.presentation.feature.HomeScreenFactory
import ru.snowadv.home_impl.presentation.feature.HomeScreenFactoryImpl
import ru.snowadv.home_impl.presentation.home.elm.HomeActorElm
import ru.snowadv.home_impl.presentation.home.elm.HomeCommandElm
import ru.snowadv.home_impl.presentation.home.elm.HomeEffectElm
import ru.snowadv.home_impl.presentation.home.elm.HomeEventElm
import ru.snowadv.home_impl.presentation.home.elm.HomeReducerElm
import ru.snowadv.home_impl.presentation.home.elm.HomeStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface HomeFeatureModule {
    @Binds
    fun bindHomeScreenFactoryImpl(homeScreenFactoryImpl: HomeScreenFactoryImpl): HomeScreenFactory
    @Binds
    fun bindHomeActorElm(homeActorElm: HomeActorElm): Actor<HomeCommandElm, HomeEventElm>
    @Binds
    fun bindHomeReducerElm(homeReducerElm: HomeReducerElm): ScreenDslReducer<HomeEventElm, HomeEventElm.Ui, HomeEventElm.Internal, HomeStateElm, HomeEffectElm, HomeCommandElm>
}