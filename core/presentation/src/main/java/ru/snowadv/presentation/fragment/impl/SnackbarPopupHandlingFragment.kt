package ru.snowadv.presentation.fragment.impl

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.snowadv.presentation.R
import ru.snowadv.presentation.fragment.PopupHandlingFragment

class SnackbarPopupHandlingFragment : PopupHandlingFragment {
    override fun Fragment.showInternetError(rootView: View) {
        showSnackbar(rootView = rootView, text = getString(R.string.internet_error))
    }

    override fun Fragment.showActionInternetError(rootView: View) {
        showSnackbar(rootView = rootView, text = getString(R.string.action_internet_error))
    }

    override fun Fragment.showInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(
            rootView = rootView,
            text = getString(R.string.internet_error),
            retryAction = action,
        )
    }

    override fun Fragment.showErrorWithRetryAndCustomText(
        rootView: View,
        action: () -> Unit,
        textResId: Int
    ) {
        showSnackbar(rootView = rootView, text = getString(textResId), retryAction = action)
    }

    override fun Fragment.showActionInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(
            rootView = rootView,
            text = getString(R.string.action_internet_error),
            retryAction = action,
        )
    }

    override fun Fragment.showInfo(rootView: View, text: String) {
        showSnackbar(rootView = rootView, text = text)
    }

    override fun Fragment.showInfo(rootView: View, textResId: Int) {
        showSnackbar(rootView = rootView, text = getString(textResId))
    }

    override fun Fragment.showShortInfo(rootView: View, textResId: Int) {
        showSnackbar(rootView = rootView, text = getString(textResId), snackBarLength = Snackbar.LENGTH_SHORT)
    }

    override fun Fragment.showShortInfo(rootView: View, text: String) {
        showSnackbar(rootView = rootView, text = text, snackBarLength = Snackbar.LENGTH_SHORT)
    }

    private fun Fragment.showSnackbar(
        rootView: View,
        text: String,
        retryAction: (() -> Unit)? = null,
        snackBarLength: Int = Snackbar.LENGTH_LONG,
    ) {
        Snackbar
            .make(rootView, text, snackBarLength)
            .let { if (retryAction != null) it.setAction(R.string.retry) { retryAction() } else it }
            .show();
    }

}