package ru.snowadv.presentation.elm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

abstract class BaseFragment<Event : Any, Effect : Any, State : Any>: Fragment(), ElmRendererDelegate<Effect, State> {

    abstract val store: Store<Event, Effect, State>
    open val resumeUiEvent: Event? = null // This event will be fired in onResume
    open val pauseUiEvent: Event? = null // This event will be fired in onPause

    private var observer: LifecycleObserver? = null

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (resumeUiEvent != null || pauseUiEvent != null) {
            viewLifecycleOwner.lifecycle.addObserver(StoreLifecycleObserver().also { observer = it })
        }
        return null
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        observer?.let { viewLifecycleOwner.lifecycle.removeObserver(it) }
        observer = null
    }

    private inner class StoreLifecycleObserver : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            pauseUiEvent?.let { store.accept(it) }
        }

        override fun onResume(owner: LifecycleOwner) {
            resumeUiEvent?.let { store.accept(it) }
        }
    }
}