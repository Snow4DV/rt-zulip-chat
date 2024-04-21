package ru.snowadv.presentation.elm

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class BaseFragment<Event : Any, Effect : Any, State : Any>: Fragment(), ElmRendererDelegate<Effect, State> {
    abstract val store: Store<Event, Effect, State>
}