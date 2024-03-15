package ru.snowadv.chat.presentation.common

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.ItemReactionBinding
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.presentation.utils.ViewInvalidatingProperty
import ru.snowadv.presentation.utils.dimToPx

typealias OnReactionClickListener = (count: Int, emojiCode: Int, userReacted: Boolean) -> Unit

internal abstract class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes)  {

    protected val reactions = HashMap<Int, ReactionView>() // Emoji code to ReactionView
    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    protected abstract val reactionsContainer: FlexBoxLayout
    protected abstract val messageTextView: TextView
    protected abstract val timestampTextView: TextView
    protected abstract val messageBackgroundView: View

    companion object {
        const val DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP = 5
        const val DEFAULT_PADDING_BETWEEN_REACTIONS_DP = 5
        const val DEFAULT_PADDING_BETWEEN_AVATAR_AND_MESSAGE_BOX_DP = 5
        const val DEFAULT_IS_INCOMING_MESSAGE = false
        const val DEFAULT_USERNAME_TEXT = ""
        const val DEFAULT_MESSAGE_TEXT = ""
        const val DEFAULT_TIMESTAMP_TEXT = ""
    }


    var onReactionClickListener: OnReactionClickListener? = null
    var paddingBetweenMessageBoxAndReactionsPx by ViewInvalidatingProperty(
        value = dimToPx(
            DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP,
            TypedValue.COMPLEX_UNIT_DIP
        ),
        requestLayout = true,
    )
    var messageText: CharSequence
        get() = messageTextView.text
        set(value) {
            messageTextView.text = value
            requestLayout()
        }
    var timestampText: CharSequence
        get() = timestampTextView.text
        set(value) {
            timestampTextView.text = value
            requestLayout()
        }
    var paddingBetweenReactionsPx
        get() = reactionsContainer.paddingBetweenViewsPx
        set(value) {
            reactionsContainer.paddingBetweenViewsPx = value
            requestLayout()
        }

    /**
     * This method says where new reactions would be inserted
     */
    protected abstract fun getInsertIndexForNewReaction(): Int

    fun addUpdateReaction(reaction: ChatReaction) {
        addUpdateReaction(reaction.emoji.code, reaction.count, reaction.userReacted)
    }

    fun addUpdateReaction(emojiCode: Int, count: Int, userReacted: Boolean) {
        updateReactionView(
            view = reactions[emojiCode]  ?: ItemReactionBinding.inflate(
                layoutInflater,
                reactionsContainer,
                false
            ).root
                .also { reactions[emojiCode] = it }
                .also { reactionsContainer.addView(it, getInsertIndexForNewReaction()) },
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted
        )
    }

    fun removeReaction(reaction: ChatReaction) {
        removeReaction(reaction.emoji.code)
    }

    fun removeReaction(emojiCode: Int) {
        reactions[emojiCode]?.let {
            reactionsContainer.removeView(it)
        }
        requestLayout()
    }

    private fun updateReactionView(view: ReactionView, reaction: ChatReaction): ReactionView {
        return updateReactionView(view, reaction.emoji.code, reaction.count, reaction.userReacted)
    }

    private fun updateReactionView(view: ReactionView, emojiCode: Int, count: Int, userReacted: Boolean): ReactionView {
        view.isSelected = userReacted
        view.count = count
        view.emojiCode = emojiCode
        view.setOnClickListener {
            onReactionClickListener?.invoke(count, emojiCode, userReacted)
        }
        requestLayout()
        return view
    }
}