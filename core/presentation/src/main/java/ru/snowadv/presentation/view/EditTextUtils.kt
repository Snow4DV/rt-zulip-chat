package ru.snowadv.presentation.view

import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object EditTextUtils {
    fun TextView.addTextChangedNotNullListener(listener: (String) -> Unit): TextWatcher {
        return addTextChangedListener {
            it?.toString()?.let { currentText ->
                listener(currentText)
            }
        }
    }

}