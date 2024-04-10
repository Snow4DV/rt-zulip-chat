package ru.snowadv.presentation.fragment

import android.view.View
import androidx.fragment.app.Fragment

interface ErrorHandlingFragment {
    fun Fragment.showInternetError(rootView: View)
    fun Fragment.showActionInternetError(rootView: View)
    fun Fragment.showInternetErrorWithRetry(rootView: View, action: () -> Unit)
    fun Fragment.showActionInternetErrorWithRetry(rootView: View, action: () -> Unit)
}