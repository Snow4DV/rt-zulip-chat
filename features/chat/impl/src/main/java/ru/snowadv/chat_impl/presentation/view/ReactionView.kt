package ru.snowadv.chat_impl.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ru.snowadv.chat_impl.R
import ru.snowadv.presentation.view.ViewInvalidatingProperty
import ru.snowadv.presentation.view.dimToPx
import kotlin.math.max
import androidx.core.content.res.use
import ru.snowadv.utils.EmojiUtils

/**
 * This view draws button with emoji and count inside. Uses background with ripple effect.
 */
internal class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.reactionButtonStyle,
    defStyleRes: Int = R.style.DefaultReactionButtonStyle,
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).also {
        it.textSize = dimToPx(DEFAULT_TEXT_AND_EMOJI_SIZE_SP, TypedValue.COMPLEX_UNIT_SP)
        it.color = DEFAULT_TEXT_COLOR
        it.isAntiAlias = true
    }
    private val textRect = Rect(0, 0, 0, 0)
    private val textToDraw: String
        get() = if (isPlus) {
            "+"
        } else {
            "${EmojiUtils.combinedHexToString(emojiCode)} $count"
        }

    companion object {
        const val DEFAULT_EMOJI_CODE = "1f603"
        const val DEFAULT_COUNT = 0
        const val DEFAULT_TEXT_AND_EMOJI_SIZE_SP = 10
        const val DEFAULT_TEXT_COLOR = Color.BLACK
        const val DEFAULT_IS_SELECTED = false
        const val DEFAULT_IS_PLUS = false
        const val DEFAULT_TEXT_TO_DRAW = "\uD83D\uDE03 0"

        fun Int.toEmojiString(): String {
            return String(Character.toChars(this))
        }
    }

    var emojiCode by ViewInvalidatingProperty(
        value = DEFAULT_EMOJI_CODE,
        requestLayout = true,
    )
    var count by ViewInvalidatingProperty(
        value = DEFAULT_COUNT,
        requestLayout = true,
    )

    var textSizePx by ViewInvalidatingProperty(
        value = dimToPx(DEFAULT_TEXT_AND_EMOJI_SIZE_SP, TypedValue.COMPLEX_UNIT_SP),
        onChange = {
            textPaint.textSize = it
        }
    )
    var textColor by ViewInvalidatingProperty(
        value = DEFAULT_TEXT_COLOR,
        onChange = {
            textPaint.color = it
        }
    )
    var isPlus by ViewInvalidatingProperty(
        value = DEFAULT_IS_PLUS,
        requestLayout = true
    )


    init {
        initAttributes(attrs, defStyleAttr, defStyleRes)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingRight + paddingLeft
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredTextHeightInPx: Int
        val desiredTextWidthInPx: Int

        if (isPlus) {
            textPaint.getTextBounds(DEFAULT_TEXT_TO_DRAW, 0, DEFAULT_TEXT_TO_DRAW.length, textRect)
            desiredTextHeightInPx = textRect.height()
            desiredTextWidthInPx = textRect.width()
            textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)
        } else {
            textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)
            desiredTextHeightInPx = textRect.height()
            desiredTextWidthInPx = textRect.width()
        }

        val desiredWidth =
            max(desiredTextWidthInPx + paddingLeft + paddingRight, minWidth)
        val desiredHeight = max(desiredTextHeightInPx + paddingTop + paddingBottom, minHeight)

        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(
            measuredWidth,
            measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()
        val leftOffset = width / 2 - textRect.exactCenterX()

        canvas.drawText(textToDraw, leftOffset, topOffset, textPaint)
    }


    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ReactionView, defStyleAttr, defStyleRes)
            .use { styledAttributes ->
                emojiCode =
                    styledAttributes.getInt(R.styleable.ReactionView_emojiCode, -1).let {
                        if (it == -1) DEFAULT_EMOJI_CODE else EmojiUtils.codePointToStringOrDefault(it)
                    }
                count = styledAttributes.getInt(R.styleable.ReactionView_count, DEFAULT_COUNT)
                textSizePx = styledAttributes.getDimensionPixelSize(
                    R.styleable.ReactionView_android_textSize,
                    dimToPx(DEFAULT_TEXT_AND_EMOJI_SIZE_SP, TypedValue.COMPLEX_UNIT_SP).toInt()
                ).toFloat()
                textColor =
                    styledAttributes.getColorStateList(R.styleable.ReactionView_android_textColor)?.defaultColor
                        ?: DEFAULT_TEXT_COLOR
                isSelected = styledAttributes.getBoolean(
                    R.styleable.ReactionView_android_state_selected,
                    DEFAULT_IS_SELECTED
                )
                isPlus = styledAttributes.getBoolean(
                    R.styleable.ReactionView_isPlus,
                    DEFAULT_IS_PLUS
                )
            }
    }
}