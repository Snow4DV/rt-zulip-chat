package ru.snowadv.chat.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import ru.snowadv.chat.databinding.ItemReactionBinding
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.presentation.util.ViewInvalidatingProperty
import ru.snowadv.presentation.util.dimToPx

typealias OnReactionClickListener = (count: Int, emojiCode: Int, userReacted: Boolean) -> Unit
typealias OnMessageLongClickListener = () -> Unit
typealias OnAddReactionClickListener = () -> Unit

internal abstract class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    protected val reactions = HashMap<Int, ReactionView>() // Emoji code to ReactionView
    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    protected abstract val reactionsContainer: FlexBoxLayout
    protected abstract val messageTextView: TextView
    protected abstract val timestampTextView: TextView
    protected abstract val messageBackgroundView: View

    companion object {
        const val DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP = 5
        const val DEFAULT_PADDING_BETWEEN_REACTIONS_DP = 5
        const val DEFAULT_MESSAGE_TEXT = ""
        const val DEFAULT_TIMESTAMP_TEXT = ""
        const val DEFAULT_MAX_WIDTH_OF_PARENT = 1.0f
    }

    var onMessageLongClickListener: OnMessageLongClickListener? = null
    var onReactionClickListener: OnReactionClickListener? = null
    var paddingBetweenMessageBoxAndReactionsPx by ViewInvalidatingProperty(
        value = dimToPx(
            DEFAULT_PADDING_BETWEEN_MESSAGE_BOX_AND_REACTIONS_DP,
            TypedValue.COMPLEX_UNIT_DIP
        ),
        requestLayout = true,
    )
    var maxWidthOfParent: Float by ViewInvalidatingProperty(
        value = DEFAULT_MAX_WIDTH_OF_PARENT,
        requestLayout = true
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
     * This function changes info about reaction in container layout
     * It creates new view if count of votes >= 1
     * It updates view if count of votes >= 1
     * It removes view if count < 1
     */
    fun addUpdateRemoveReaction(reaction: ChatReaction) {
        addUpdateRemoveReaction(
            reaction.emoji.code,
            reaction.count,
            reaction.userReacted
        )
    }

    /**
     * This function changes info about reaction in container layout
     * It creates new view if count of votes >= 1
     * It updates view if count of votes >= 1
     * It removes view if count < 1
     */
    fun addUpdateRemoveReaction(
        emojiCode: Int,
        count: Int,
        userReacted: Boolean
    ) {
        addUpdateRemoveReaction(emojiCode, count, userReacted, true)
    }

    private fun addUpdateRemoveReaction(reaction: ChatReaction, requestLayout: Boolean) {
        addUpdateRemoveReaction(
            reaction.emoji.code,
            reaction.count,
            reaction.userReacted,
            requestLayout
        )
    }

    private fun addUpdateRemoveReaction(
        emojiCode: Int,
        count: Int,
        userReacted: Boolean,
        requestLayout: Boolean
    ) {
        if (count <= 0) {
            reactions[emojiCode]?.let {
                reactionsContainer.removeView(it)
            }
        } else {
            updateReactionView(
                view = reactions[emojiCode] ?: ItemReactionBinding.inflate(
                    layoutInflater,
                    reactionsContainer,
                    false
                ).root
                    .also { reactions[emojiCode] = it }
                    .also { reactionsContainer.addView(it) },
                emojiCode = emojiCode,
                count = count,
                userReacted = userReacted,
                requestLayout = requestLayout
            )
        }
    }

    fun clearReactions() {
        reactions.clear()
        reactionsContainer.children
            .filterIsInstance(ReactionView::class.java)
            .filter { !it.isPlus }
            .toList().forEach {
                reactionsContainer.removeView(it)
            }
        requestLayout()
    }


    /**
     * This function replaces reactions by making least amount of replaces possible
     */
    fun replaceReactions(reactions: List<ChatReaction>) {
        // Update views to match new reactions list
        val reactionsSet = reactions.asSequence().filter { it.count > 0 }.map { it.emoji.code }.toSet()

        this.reactions.keys.filter { it !in reactionsSet }.forEach {
            removeReactionView(it, false)
        }

        reactions.forEach {
            addUpdateRemoveReaction(it, false)
        }

        // Reorder views
        val viewsMap = children
            .mapNotNull { it as? ReactionView }
            .filter { !it.isPlus && it.visibility != GONE }
            .associateBy { it.emojiCode }

        reactionsContainer.removeAllViews()

        reactions.forEach {
            viewsMap[it.emoji.code]?.let {reactionView ->
                reactionsContainer.addView(reactionView)
            }
        }

        // Add plus at the end if reactionsSet is not empty
        if (reactionsSet.isNotEmpty()) {
            reactionsContainer.addView(getAddReactionButtonView().also {  plusView ->
                plusView.setOnClickListener {  }
            })
        }
    }

    private fun removeReactionView(reaction: ChatReaction, requestLayout: Boolean = true) {
        removeReactionView(reaction.emoji.code, requestLayout)
    }

    private fun removeReactionView(emojiCode: Int, requestLayout: Boolean = true) {
        reactions[emojiCode]?.let {
            reactionsContainer.removeView(it)
        }
        if (requestLayout) requestLayout()
    }

    private fun updateReactionView(view: ReactionView, reaction: ChatReaction): ReactionView {
        return updateReactionView(view, reaction.emoji.code, reaction.count, reaction.userReacted)
    }

    private fun updateReactionView(
        view: ReactionView,
        emojiCode: Int,
        count: Int,
        userReacted: Boolean,
        requestLayout: Boolean = true
    ): ReactionView {
        view.isSelected = userReacted
        view.count = count
        view.emojiCode = emojiCode
        view.setOnClickListener {
            onReactionClickListener?.invoke(count, emojiCode, userReacted)
        }
        if (requestLayout) requestLayout()
        return view
    }

    private fun getAddReactionButtonView(): ReactionView {
        return ItemReactionBinding.inflate(
            layoutInflater,
            reactionsContainer,
            false
        ).root
            .also { it.isPlus = true }
    }
}