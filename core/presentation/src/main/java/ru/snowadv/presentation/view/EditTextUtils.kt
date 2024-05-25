package ru.snowadv.presentation.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.transformLatest

object EditTextUtils {
    const val DEFAULT_INPUT_DEBOUNCE = 150L
    fun TextView.addTextChangedNotNullListener(listener: (String) -> Unit): TextWatcher {
        return addTextChangedListener {
            it?.toString()?.let { currentText ->
                listener(currentText)
            }
        }
    }

    fun TextView.afterTextChanged(callback: (text: String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                callback(s?.toString() ?: "")
            }
        })
    }
    fun TextView.observe(debounce: Long = DEFAULT_INPUT_DEBOUNCE): Flow<String> {
        return callbackFlow {
            val listener = doOnTextChanged { text, _, _, _ -> text?.toString()?.let { trySend(it) } }
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text.toString()) }.debounce(debounce)
    }

    // This is needed to debounce with a predicate: for example, if text field is changed to non-empty
    // you might want to react to this change instantly (without debounce)
    inline fun TextView.observe(debounce: Long = DEFAULT_INPUT_DEBOUNCE, crossinline noDebouncePredicate: (String) -> Boolean): Flow<String> {
        return callbackFlow {
            val listener = doOnTextChanged { text, _, _, _ -> text?.toString()?.let { trySend(it) } }
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text.toString()) }.transformLatest {
            if (noDebouncePredicate(it)) {
                emit(it)
            } else {
                delay(debounce)
                emit(it)
            }
        }
    }
}