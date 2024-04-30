package ru.snowadv.home_impl.presentation.home.elm

import ru.snowadv.presentation.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class HomeReducerElm @Inject constructor() : ScreenDslReducer<HomeEventElm, HomeEventElm.Ui, HomeEventElm.Internal, HomeStateElm, HomeEffectElm, HomeCommandElm>(
    uiEventClass = HomeEventElm.Ui::class,
    internalEventClass = HomeEventElm.Internal::class,
) {
    override fun Result.internal(event: HomeEventElm.Internal) {}

    override fun Result.ui(event: HomeEventElm.Ui): Any =
        when(event) {
            is HomeEventElm.Ui.OnBottomTabSelected -> state {
                copy(currentScreen = event.screen)
            }
        }
}