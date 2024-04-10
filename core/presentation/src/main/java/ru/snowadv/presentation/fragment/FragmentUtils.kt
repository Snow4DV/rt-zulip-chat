package ru.snowadv.presentation.fragment

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.snowadv.presentation.databinding.ItemActionLoadingBinding
import ru.snowadv.presentation.databinding.ItemEmptyBoxBinding
import ru.snowadv.presentation.databinding.ItemErrorBoxBinding
import ru.snowadv.presentation.databinding.ItemLoadingBoxBinding
import ru.snowadv.presentation.databinding.ItemStateBoxBinding
import ru.snowadv.presentation.databinding.ItemTopBarWithBackBinding
import ru.snowadv.presentation.model.ScreenState

fun ItemStateBoxBinding.inflateState(screenState: ScreenState<*>, shimmerLayout: Int? = null) = with(root) {
    when(screenState) {
        is ScreenState.Empty -> {
            removeChildViewsAndSetVisibility(visibility = true)
            ItemEmptyBoxBinding.inflate(LayoutInflater.from(context), this, true)
        }
        is ScreenState.Error -> {
            removeChildViewsAndSetVisibility(visibility = true)
            ItemErrorBoxBinding.inflate(LayoutInflater.from(context), this, true).also {
                it.retryErrorButton.setOnClickListener { callOnClick() }
            }
        }
        is ScreenState.Loading -> {
            removeChildViewsAndSetVisibility(visibility = true)
            shimmerLayout?.let { LayoutInflater.from(context).inflate(shimmerLayout, this) } ?: run {
                ItemLoadingBoxBinding.inflate(LayoutInflater.from(context), this, true)
            }
        }
        is ScreenState.Success -> {
            removeChildViewsAndSetVisibility(visibility = false)
        }
    }
}

fun ItemStateBoxBinding.setOnRetryClickListener(listener: ((View) -> Unit)?) {
    root.setOnClickListener(listener)
}

fun ItemStateBoxBinding.removeChildViewsAndSetVisibility(visibility: Boolean) {
    root.removeAllViews()
    root.isVisible = visibility
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