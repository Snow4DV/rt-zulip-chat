package ru.snowadv.presentation.fragment.impl

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.snowadv.presentation.R
import ru.snowadv.presentation.fragment.PopupHandlingFragment

class SnackbarPopupHandlingFragment: PopupHandlingFragment {
    override fun Fragment.showInternetError(rootView: View) {
        showSnackbar(rootView, getString(R.string.internet_error))
    }

    override fun Fragment.showActionInternetError(rootView: View) {
        showSnackbar(rootView, getString(R.string.action_internet_error))
    }

    override fun Fragment.showInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(rootView, getString(R.string.internet_error), action)
    }

    override fun Fragment.showErrorWithRetryAndCustomText(
        rootView: View,
        action: () -> Unit,
        textResId: Int
    ) {
        showSnackbar(rootView, getString(textResId), action)
    }

    override fun Fragment.showActionInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(rootView, getString(R.string.action_internet_error), action)
    }

    override fun Fragment.showInfo(rootView: View, text: String) {
        showSnackbar(rootView, text)
    }

    override fun Fragment.showInfo(rootView: View, textResId: Int) {
        showSnackbar(rootView, getString(textResId))
    }

    private fun Fragment.showSnackbar(rootView: View, text: String, retryAction: (() -> Unit)? = null) {
        Snackbar
            .make(rootView, text, Snackbar.LENGTH_LONG)
            .let { if (retryAction != null) it.setAction(R.string.retry) { retryAction() } else it }
            .show();
    }

}