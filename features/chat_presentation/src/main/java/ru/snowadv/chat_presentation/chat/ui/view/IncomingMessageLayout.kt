package ru.snowadv.chat_presentation.chat.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import ru.snowadv.presentation.view.ViewInvalidatingProperty
import ru.snowadv.presentation.view.dimToPx
import kotlin.math.max
import kotlin.math.min
import androidx.core.content.res.use
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.databinding.ItemMessageViewIncomingBinding


/**
 * This view draws message with name, text, timestamp, avatar and emojis flexbox for incoming
 * message
 */
class IncomingMessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.outgoingMessageLayoutStyle,
    defStyleRes: Int = 0,
) :
    MessageLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ItemMessageViewIncomingBinding =
        ItemMessageViewIncomingBinding.inflate(layoutInflater, this)

    override val messageBackgroundView: View = binding.messageBackground
    override val reactionsContainer: FlexBoxLayout = binding.reactionsContainer
    override val messageTextView: TextView = binding.messageText
    override val timestampTextView: TextView = binding.messageTimestamp

    val avatarImageView
        get() = binding.avatar

    companion object {
        const val DEFAULT_PADDING_BETWEEN_AVATAR_AND_MESSAGE_BOX_DP = 5
        const val DEFAULT_USERNAME_TEXT = ""
        const val DEFAULT_TIMESTAMP_TEXT = ""
    }

    var paddingBetweenAvatarAndMessageBoxPx by ViewInvalidatingProperty(
        value = dimToPx(
            DEFAULT_PADDING_BETWEEN_AVATAR_AND_MESSAGE_BOX_DP,
            TypedValue.COMPLEX_UNIT_DIP
        ),
        requestLayout = true,
    )
    var usernameText: CharSequence
        get() = binding.messageUserName.text
        set(value) {
            binding.messageUserName.text = value
            requestLayout()
        }
    var timeText: CharSequence
        get() = binding.messageTimestamp.text
        set(value) {
            binding.messageTimestamp.text = value
            requestLayout()
        }
    var avatar: Drawable?
        get() = binding.avatar.drawable
        set(value) {
            binding.avatar.setImageDrawable(value)
            requestLayout()
        }

    init {
        id = R.id.incoming_message
        initAttributes(attrs, defStyleAttr, defStyleRes)
        initLongClickListener()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingRight + paddingLeft
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        measureChild(binding.avatar, widthMeasureSpec, heightMeasureSpec) // Measure avatar

        val avatarAndPaddingWidth =
            binding.avatar.measuredWidth + paddingBetweenAvatarAndMessageBoxPx.toInt()

        val messageItemsWidth = ((MeasureSpec.getSize(widthMeasureSpec) -
                paddingLeft - paddingRight - avatarAndPaddingWidth) * maxWidthOfParent).toInt()
        val messageBoxWidthMeasureSpec =
            MeasureSpec.makeMeasureSpec(messageItemsWidth, MeasureSpec.getMode(widthMeasureSpec))
        for (child in children) {
            if (child === binding.avatar) continue // Avatar is already measured

            measureChild(child, messageBoxWidthMeasureSpec, heightMeasureSpec)
        }

        // Margins of views are ignored because they are not used in layout
        val widthPx = with(binding) {
            avatar.measuredWidth + maxOf(
                messageUserName.measuredWidth,
                messageText.measuredWidth,
                messageTimestamp.measuredWidth,
                reactionsContainer.measuredWidth,
            ) + paddingBetweenAvatarAndMessageBoxPx + paddingLeft + paddingRight
        }.toInt()
        val heightPx = with(binding) {
            max(
                avatar.measuredHeight,
                messageUserName.measuredHeight + messageText.measuredHeight
                        + messageTimestamp.measuredHeight + reactionsContainer.measuredHeight
                        + if (reactionsContainer.measuredHeight > 0) paddingBetweenMessageBoxAndReactionsPx.toInt() else 0
            ) + paddingTop + paddingBottom
        }

        val desiredWidth = max(widthPx, minWidth)
        val desiredHeight = max(heightPx, minHeight)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val containerLeft = paddingLeft
        val containerTop = paddingTop
        val containerRight = measuredWidth - paddingRight
        val containerBottom = measuredHeight - paddingBottom
        // Margins of views are ignored because they are not used in layout
        binding.apply {
            avatar.layout(
                containerLeft,
                containerTop,
                containerLeft + avatar.measuredWidth,
                containerTop + avatar.measuredHeight
            )

            val messageBoxLeft =
                containerLeft + avatar.measuredWidth + paddingBetweenAvatarAndMessageBoxPx.toInt()

            val messageBoxMeasuredWidth = maxOf(
                messageTextView.measuredWidth,
                timestampTextView.measuredWidth,
                messageUserName.measuredWidth,
            )

            val messageBoxRight = min(messageBoxLeft + messageBoxMeasuredWidth, containerRight)

            val reactionsContainerTop = messageBackground.layout(
                messageBoxLeft,
                containerTop,
                messageBoxRight,
                containerTop + messageUserName.measuredHeight + messageText.measuredHeight + messageTimestamp.measuredHeight
            ).let { messageBackground.bottom + paddingBetweenMessageBoxAndReactionsPx.toInt() }

            val messageTextTop = if (messageUserName.visibility != GONE) {
                messageUserName.layout(
                    messageBoxLeft,
                    containerTop,
                    messageBoxRight,
                    containerTop + messageUserName.measuredHeight,
                )
                containerTop + messageUserName.measuredHeight
            } else {
                containerTop
            }

            val messageTimestampTop = messageText.layout(
                messageBoxLeft,
                messageTextTop,
                messageBoxRight,
                messageTextTop + messageText.measuredHeight
            ).let { messageText.bottom }

            messageTimestamp.layout(
                messageBoxRight - messageTimestamp.measuredWidth,
                messageTimestampTop,
                messageBoxRight,
                messageTimestampTop + messageTimestamp.measuredHeight

            )

            reactionsContainer.layout(
                messageBoxLeft,
                reactionsContainerTop,
                containerRight,
                containerBottom
            )
        }
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.IncomingMessageLayout,
            defStyleAttr,
            defStyleRes
        )
            .use { styledAttributes ->
                paddingBetweenReactionsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.IncomingMessageLayout_paddingBetweenReactions,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                paddingBetweenMessageBoxAndReactionsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.IncomingMessageLayout_paddingBetweenMessageBoxAndReactions,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                paddingBetweenAvatarAndMessageBoxPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.IncomingMessageLayout_paddingBetweenAvatarAndMessageBox,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_AVATAR_AND_MESSAGE_BOX_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                avatar =
                    styledAttributes.getDrawable(R.styleable.IncomingMessageLayout_avatar)
                usernameText =
                    styledAttributes.getString(R.styleable.IncomingMessageLayout_usernameText)
                        ?: DEFAULT_USERNAME_TEXT
                messageText =
                    styledAttributes.getString(R.styleable.IncomingMessageLayout_messageText)
                        ?: DEFAULT_MESSAGE_TEXT
                timeText =
                    styledAttributes.getString(R.styleable.IncomingMessageLayout_timestampText)
                        ?: DEFAULT_TIMESTAMP_TEXT
                avatar =
                    styledAttributes.getDrawable(R.styleable.IncomingMessageLayout_avatar)
                        ?: getDefaultAvatar()
                maxWidthOfParent = styledAttributes.getFraction(
                    R.styleable.IncomingMessageLayout_maxWidthOfParent,
                    1,
                    1,
                    DEFAULT_MAX_WIDTH_OF_PARENT
                )
            }
    }

    private fun getDefaultAvatar(): Drawable {
        return ResourcesCompat.getDrawable(
            resources,
            ru.snowadv.presentation.R.drawable.ic_user_avatar,
            context.theme
        ) ?: error("Missing default avatar resource")
    }

    private fun initLongClickListener() {
        messageBackgroundView.setOnLongClickListener {
            onMessageLongClickListener?.invoke()
            true
        }
        messageTextView.setOnLongClickListener {
            onMessageLongClickListener?.invoke()
            true
        }
    }
}