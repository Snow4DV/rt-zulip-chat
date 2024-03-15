package ru.snowadv.chat.presentation.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.ItemMessageIncomingBinding
import ru.snowadv.chat.databinding.ItemReactionBinding
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.presentation.utils.ViewInvalidatingProperty
import ru.snowadv.presentation.utils.dimToPx
import kotlin.math.max

typealias OnAddReactionClickListener = () -> Unit

/**
 * This view draws message with name, text, timestamp, avatar and emojis flexbox for incoming
 * message
 */
internal class IncomingMessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.outgoingMessageLayoutStyle,
    defStyleRes: Int = 0,
) :
    MessageLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ItemMessageIncomingBinding = ItemMessageIncomingBinding.inflate(layoutInflater, this)

    override val messageBackgroundView: View = binding.messageBackground

    override val reactionsContainer: FlexBoxLayout = binding.reactionsContainer
    override val messageTextView: TextView = binding.messageText
    override val timestampTextView: TextView = binding.messageTimestamp

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
    var onAddReactionClickListener: OnAddReactionClickListener? = null

    init {
        initAttributes(attrs, defStyleAttr, defStyleRes)
        initListenerForAddReactionButton()
    }

    override fun getInsertIndexForNewReaction(): Int {
        return binding.reactionsContainer.childCount - 1 // New reactions are inserted before add reaction button
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingRight + paddingLeft
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        measureChild(binding.avatar, widthMeasureSpec, heightMeasureSpec) // Measure avatar

        val avatarAndPaddingWidth = binding.avatar.measuredWidth + paddingBetweenAvatarAndMessageBoxPx.toInt()

        val messageItemsWidth =
            MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight - avatarAndPaddingWidth
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
                messageTimestamp.measuredWidth
            ) + paddingBetweenAvatarAndMessageBoxPx + paddingLeft + paddingRight
        }.toInt()
        val heightPx = with(binding) {
            max(
                avatar.measuredHeight,
                messageUserName.measuredHeight + messageText.measuredHeight
                        + messageTimestamp.measuredHeight + reactionsContainer.measuredHeight
                        + paddingBetweenMessageBoxAndReactionsPx.toInt()
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
                avatar.measuredWidth,
                avatar.measuredHeight
            )

            val messageBoxLeft = avatar.measuredWidth + paddingBetweenAvatarAndMessageBoxPx.toInt()

            val reactionsContainerTop = messageBackground.layout(
                messageBoxLeft,
                containerTop,
                containerRight,
                containerTop + messageUserName.measuredHeight + messageText.measuredHeight + messageTimestamp.measuredHeight
            ).let { messageBackground.bottom + paddingBetweenMessageBoxAndReactionsPx.toInt() }

            val messageTextTop = if (messageUserName.visibility != GONE) {
                messageUserName.layout(
                    messageBoxLeft,
                    containerTop,
                    containerRight,
                    containerTop + messageUserName.measuredHeight,
                )
                containerTop + messageUserName.measuredHeight
            } else {
                containerTop
            }

            val messageTimestampTop = messageText.layout(
                messageBoxLeft,
                messageTextTop,
                containerRight,
                messageTextTop + messageText.measuredHeight
            ).let { messageText.bottom }

            messageTimestamp.layout(
                messageBoxLeft,
                messageTimestampTop,
                containerRight,
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
                    R.styleable.IncomingMessageLayout_paddingBetweenReactionsInIncomingMessage,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                paddingBetweenMessageBoxAndReactionsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.IncomingMessageLayout_paddingBetweenMessageBoxAndReactionsInIncomingMessage,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                paddingBetweenAvatarAndMessageBoxPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.IncomingMessageLayout_paddingBetweenAvatarAndMessageBoxInIncomingMessage,
                    dimToPx(
                        DEFAULT_PADDING_BETWEEN_AVATAR_AND_MESSAGE_BOX_DP,
                        TypedValue.COMPLEX_UNIT_DIP
                    ).toInt()
                ).toFloat()
                avatar = styledAttributes.getDrawable(R.styleable.IncomingMessageLayout_avatarInIncomingMessage)
                usernameText = styledAttributes.getString(R.styleable.IncomingMessageLayout_usernameTextInIncomingMessage)
                    ?: DEFAULT_USERNAME_TEXT
                messageText = styledAttributes.getString(R.styleable.IncomingMessageLayout_messageTextInIncomingMessage)
                    ?: DEFAULT_MESSAGE_TEXT
                timeText = styledAttributes.getString(R.styleable.IncomingMessageLayout_timestampTextInIncomingMessage)
                    ?: DEFAULT_TIMESTAMP_TEXT
                avatar = styledAttributes.getDrawable(R.styleable.IncomingMessageLayout_avatarInIncomingMessage)
                    ?: getDefaultAvatar()
            }
    }

    private fun initListenerForAddReactionButton() {
        binding.addReactionButton.setOnClickListener {
            onAddReactionClickListener?.invoke()
        }
    }

    private fun getDefaultAvatar(): Drawable {
        return ResourcesCompat.getDrawable(
            resources,
            ru.snowadv.presentation.R.drawable.ic_user_avatar,
            context.theme
        ) ?: error("Missing default avatar resource")
    }
}