package ru.snowadv.presentation.elm

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class BaseBottomSheetDialogFragment<Event : Any, Effect : Any, State : Any>: BottomSheetDialogFragment(), ElmRendererDelegate<Effect, State> {
    abstract val store: Store<Event, Effect, State>
}