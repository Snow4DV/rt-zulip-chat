package ru.snowadv.presentation.elm

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.presentation.R
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store


abstract class BaseBottomSheetDialogFragment<Event : Any, Effect : Any, State : Any> : BottomSheetDialogFragment(), ElmRendererDelegate<Effect, State> {
    abstract val store: Store<Event, Effect, State>

    override fun getTheme(): Int = R.style.Theme_ZulipChat_BottomSheet
}