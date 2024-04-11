package ru.snowadv.chat.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.ItemMessageViewOutgoingBinding
import ru.snowadv.presentation.view.dimToPx
import kotlin.math.max
import androidx.core.content.res.use

/**
 * This view draws message with text, timestamp and emojis flexbox for outgoing message
 */
internal class OutgoingMessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.outgoingMessageLayoutStyle,
    defStyleRes: Int = 0,
) :
    MessageLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ItemMessageViewOutgoingBinding =
        ItemMessageViewOutgoingBinding.inflate(layoutInflater, this)

    override val messageBackgroundView: View = binding.messageBackground
    override val reactionsContainer: FlexBoxLayout = binding.reactionsContainer
    override val messageTextView: TextView = binding.messageText
    override val timestampTextView: TextView = binding.messageTimestamp

    init {
        initAttributes(attrs, defStyleAttr, defStyleRes)
        initLongClickListener()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingRight + paddingLeft
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            (MeasureSpec.getSize(widthMeasureSpec) * maxWidthOfParent).toInt(),
            MeasureSpec.getMode(widthMeasureSpec)
        )
        measureChildren(newWidthMeasureSpec, heightMeasureSpec)

        val widthPx = maxOf(
            messageTextView.measuredWidth,
            timestampTextView.measuredWidth
        ) + paddingLeft + paddingRight

        val heightPx =
            messageTextView.measuredHeight + timestampTextView.measuredHeight +
                    reactionsContainer.measuredHeight + paddingTop + paddingBottom +
                    if (reactionsContainer.measuredHeight > 0) paddingBetweenMessageBoxAndReactionsPx.toInt() else 0

        val desiredWidth = max(widthPx, minWidth)
        val desiredHeight = max(heightPx, minHeight)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxMessageMeasuredWidth = max(
            messageTextView.measuredWidth,
            timestampTextView.measuredWidth,
        )

        val containerTop = paddingTop
        val containerRight = measuredWidth - paddingRight
        val containerBottom = measuredHeight - paddingBottom
        // Margins of views are ignored because they are not used in layout
        val reactionsContainerTop = messageBackgroundView.layout(
            containerRight - maxMessageMeasuredWidth,
            containerTop,
            containerRight,
            containerTop + messageTextView.measuredHeight + timestampTextView.measuredHeight
        ).let { messageBackgroundView.bottom + paddingBetweenMessageBoxAndReactionsPx.toInt() }

        val messageTimestampTop = messageTextView.layout(
            containerRight - maxMessageMeasuredWidth,
            containerTop,
            containerRight,
            containerTop + messageTextView.measuredHeight
        ).let { messageTextView.bottom }

        timestampTextView.layout(
            containerRight - timestampTextView.measuredWidth,
            messageTimestampTop,
            containerRight,
            messageTimestampTop + timestampTextView.measuredHeight

        )

        reactionsContainer.layout(
            containerRight - reactionsContainer.measuredWidth,
            reactionsContainerTop,
            containerRight,
            containerBottom
        )
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.OutgoingMessageLayout,
            defStyleAttr,
            defStyleRes
        )
            .use { styledAttributes ->
                paddingBetweenReactionsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.OutgoingMessageLayout_paddingBetweenReactions,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                paddingBetweenMessageBoxAndReactionsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.OutgoingMessageLayout_paddingBetweenMessageBoxAndReactions,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                messageText =
                    styledAttributes.getString(R.styleable.OutgoingMessageLayout_messageText)
                        ?: DEFAULT_MESSAGE_TEXT
                timestampText =
                    styledAttributes.getString(R.styleable.OutgoingMessageLayout_timestampText)
                        ?: DEFAULT_TIMESTAMP_TEXT
                maxWidthOfParent = styledAttributes.getFraction(
                    R.styleable.OutgoingMessageLayout_maxWidthOfParent,
                    1,
                    1,
                    DEFAULT_MAX_WIDTH_OF_PARENT
                )
            }
    }

    private fun initLongClickListener() {
        messageBackgroundView.setOnClickListener {
            onMessageLongClickListener?.invoke()
        }
    }
}