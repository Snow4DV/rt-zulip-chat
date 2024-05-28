package ru.snowadv.presentation.fragment

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import vivid.money.elmslie.core.store.Store

interface ElmFragmentRenderer<ViewFragment : Fragment, Binding: ViewBinding, Event : Any, Effect : Any, State: Any> {
    fun ViewFragment.onAttachRendererView() {}
    fun ViewFragment.onRendererViewCreated(binding: Binding, store: Store<Event, Effect, State>) {}
    fun ViewFragment.renderStateByRenderer(state: State, binding: Binding) {}
    fun ViewFragment.handleEffectByRenderer(effect: Effect, binding: Binding, store: Store<Event, Effect, State>) {}
    fun ViewFragment.onDestroyRendererView() {}
}