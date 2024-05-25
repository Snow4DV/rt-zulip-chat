package ru.snowadv.presentation.fragment

import android.view.View
import androidx.fragment.app.Fragment

interface PopupHandlingFragment {
    fun Fragment.showInternetError(rootView: View)
    fun Fragment.showActionInternetError(rootView: View)
    fun Fragment.showInternetErrorWithRetry(rootView: View, action: () -> Unit)
    fun Fragment.showErrorWithRetryAndCustomText(rootView: View, action: () -> Unit, textResId: Int)
    fun Fragment.showActionInternetErrorWithRetry(rootView: View, action: () -> Unit)
    fun Fragment.showInfo(rootView: View, textResId: Int)
    fun Fragment.showInfo(rootView: View, text: String)
    fun Fragment.showShortInfo(rootView: View, textResId: Int)
    fun Fragment.showShortInfo(rootView: View, text: String)
}