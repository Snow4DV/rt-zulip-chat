package ru.snowadv.presentation.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.databinding.ItemEmptyBoxBinding
import ru.snowadv.presentation.databinding.ItemErrorBoxBinding
import ru.snowadv.presentation.databinding.ItemErrorWithCachedDataBinding
import ru.snowadv.presentation.databinding.ItemLoadingBoxBinding
import ru.snowadv.presentation.databinding.ItemLoadingWithCachedDataBinding
import ru.snowadv.presentation.databinding.ItemStateBoxBinding
import ru.snowadv.presentation.databinding.ItemTopBarWithBackBinding
import ru.snowadv.presentation.databinding.ItemTopStateBoxBinding

fun ItemStateBoxBinding.inflateState(
    screenState: ScreenState<*>,
    shimmerLayout: Int? = null,
    cacheStateBinding: ItemTopStateBoxBinding? = null,
    showLoadingInCacheState: Boolean = true,
) {
    with(root) {
        when (screenState) {
            is ScreenState.Empty -> {
                setHolder(this@inflateState, cacheStateBinding, StateHolder.BOX)
                ItemEmptyBoxBinding.inflate(LayoutInflater.from(context), this, true)
            }

            is ScreenState.Error -> {
                if (screenState.data == null || cacheStateBinding == null) {
                    setHolder(this@inflateState, cacheStateBinding, StateHolder.BOX)
                    ItemErrorBoxBinding.inflate(LayoutInflater.from(context), this, true).also {
                        it.retryErrorButton.setOnClickListener { callOnClick() }
                    }
                } else {
                    setHolder(this@inflateState, cacheStateBinding, StateHolder.CACHE_BAR)
                    ItemErrorWithCachedDataBinding.inflate(
                        LayoutInflater.from(context),
                        cacheStateBinding.root,
                        true
                    ).also {
                        it.root.setOnClickListener { callOnClick() }
                    }
                }
            }

            is ScreenState.Loading -> {
                if (screenState.data == null || cacheStateBinding == null) {
                    setHolder(this@inflateState, cacheStateBinding, StateHolder.BOX)
                    shimmerLayout?.let { LayoutInflater.from(context).inflate(shimmerLayout, this) }
                        ?: run {
                            ItemLoadingBoxBinding.inflate(LayoutInflater.from(context), this, true)
                        }
                } else {
                    setHolder(this@inflateState, cacheStateBinding, StateHolder.CACHE_BAR)
                    if (showLoadingInCacheState) {
                        ItemLoadingWithCachedDataBinding.inflate(
                            LayoutInflater.from(context),
                            cacheStateBinding.root,
                            true
                        ).also {
                            it.root.setOnClickListener { callOnClick() }
                        }
                    } else {
                        Unit
                    }
                }
            }

            is ScreenState.Success -> {
                setHolder(this@inflateState, cacheStateBinding, StateHolder.NONE)
            }
        }
    }
}

fun ItemStateBoxBinding.setOnRetryClickListener(listener: ((View) -> Unit)?) {
    root.setOnClickListener(listener)
}

fun ViewGroup.removeChildViewsAndSetVisibility(visibility: Boolean) {
    if (childCount > 0) removeAllViews()
    isVisible = visibility
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

inline fun <reified T> Bundle.getParcelableTypeSafe(key: String): T? {
    return BundleCompat.getParcelable(this, key, T::class.java)
}

fun Fragment.setStatusBarColor(colorResId: Int) {
    requireActivity().window.statusBarColor = requireContext().getColor(colorResId)
}

private fun setHolder(
    boxStateBinding: ItemStateBoxBinding,
    cacheStateBinding: ItemTopStateBoxBinding? = null,
    holder: StateHolder
) {
    when (holder) {
        StateHolder.BOX -> {
            boxStateBinding.root.removeChildViewsAndSetVisibility(visibility = true)
            cacheStateBinding?.root?.removeChildViewsAndSetVisibility(visibility = false)
        }

        StateHolder.CACHE_BAR -> {
            boxStateBinding.root.removeChildViewsAndSetVisibility(visibility = false)
            cacheStateBinding?.root?.removeChildViewsAndSetVisibility(visibility = true)
        }

        StateHolder.NONE -> {
            boxStateBinding.root.removeChildViewsAndSetVisibility(visibility = false)
            cacheStateBinding?.root?.removeChildViewsAndSetVisibility(visibility = false)
        }
    }
}

private enum class StateHolder {
    BOX,
    CACHE_BAR,
    NONE,
}