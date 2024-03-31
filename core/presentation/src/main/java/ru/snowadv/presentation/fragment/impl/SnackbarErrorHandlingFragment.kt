package ru.snowadv.presentation.fragment.impl

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.snowadv.presentation.R
import ru.snowadv.presentation.fragment.ErrorHandlingFragment

class SnackbarErrorHandlingFragment: ErrorHandlingFragment {
    override fun Fragment.showInternetError(rootView: View) {
        showSnackbar(rootView, getString(R.string.internet_error))
    }

    override fun Fragment.showActionInternetError(rootView: View) {
        showSnackbar(rootView, getString(R.string.action_internet_error))
    }

    override fun Fragment.showInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(rootView, getString(R.string.internet_error), action)
    }

    override fun Fragment.showActionInternetErrorWithRetry(rootView: View, action: () -> Unit) {
        showSnackbar(rootView, getString(R.string.action_internet_error), action)
    }

    private fun Fragment.showSnackbar(rootView: View, text: String, retryAction: (() -> Unit)? = null) {
        Snackbar
            .make(rootView, text, Snackbar.LENGTH_LONG)
            .let { if (retryAction != null) it.setAction(R.string.retry) { retryAction() } else it }
            .show();
    }

}