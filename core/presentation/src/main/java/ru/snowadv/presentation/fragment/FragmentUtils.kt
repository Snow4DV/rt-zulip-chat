package ru.snowadv.presentation.fragment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import ru.snowadv.presentation.databinding.ItemActionLoadingBinding
import ru.snowadv.presentation.databinding.ItemStateBoxBinding
import ru.snowadv.presentation.databinding.ItemTopBarWithBackBinding
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.view.setVisibility

fun ItemStateBoxBinding.setNewState(screenState: ScreenState<*>, customLoading: Boolean = false) {
    when(screenState) {
        is ScreenState.Empty -> {
            setVisibleView(emptyBox.root)
        }
        is ScreenState.Error -> {
            setVisibleView(errorBox.root)
        }
        is ScreenState.Loading -> {
            // Loading screen can be replaced in screen with custom one (for example, with shimmer-based)
            setVisibleView(if (!customLoading) loadingBox.root else null)
        }
        is ScreenState.Success -> {
            setVisibleView(null)
        }
    }
}

fun ItemActionLoadingBinding.setNewState(actionInProgress: Boolean) {
    root.visibility = if (actionInProgress) View.VISIBLE else View.GONE
}

fun ItemStateBoxBinding.setOnRetryClickListener(listener: ((View) -> Unit)?) {
    errorBox.retryErrorButton.setOnClickListener(listener)
}

fun ItemTopBarWithBackBinding.setTopBarColor(colorResId: Int) {
    val context = root.context
    this.root.background = ColorDrawable(context.resources.getColor(colorResId, context.theme))
}
fun ItemTopBarWithBackBinding.setTopBarText(text: String) {
    this.barTitle.text = text
}

fun ItemTopBarWithBackBinding.setTopBarTextByRes(textResId: Int) {
    this.barTitle.text = root.context.getString(textResId)
}

fun ItemTopBarWithBackBinding.setColorAndText(colorResId: Int, textResId: Int) {
    setTopBarText(root.context.getString(textResId))
    setTopBarColor(colorResId)
}

fun ItemTopBarWithBackBinding.setColorAndText(colorResId: Int, text: String) {
    setTopBarText(text)
    setTopBarColor(colorResId)
}

fun Fragment.setStatusBarColor(colorResId: Int) {
    requireActivity().window.statusBarColor = requireContext().getColor(colorResId)
}

private fun ItemStateBoxBinding.setVisibleView(view: View?) {
    root.children.forEach {
        it.visibility = if (it === view) View.VISIBLE else View.GONE
    }
    this.root.setVisibility(view != null)
}