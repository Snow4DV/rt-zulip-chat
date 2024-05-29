package ru.snowadv.chat_presentation.chat.ui.markwon

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.TextView
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class NoLongClickLinkMovementMethod @Inject constructor() : LinkMovementMethod() {
    private val longClickDelay = 200L
    private var lastClickTime = 0L

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        MotionEvent.ACTION_DOWN
        when(val action = event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastClickTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - lastClickTime >= longClickDelay) {
                    return true
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }
}