package ru.snowadv.chat_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.children
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import ru.snowadv.chat_impl.databinding.ItemReactionBinding
import ru.snowadv.chat_impl.presentation.model.ChatReaction
import ru.snowadv.presentation.view.ViewInvalidatingProperty
import ru.snowadv.presentation.view.dimToPx
import kotlin.math.max
import kotlin.math.min

typealias OnReactionClickListener = (count: Int, emojiCode: String, userReacted: Boolean) -> Unit
typealias OnMessageLongClickListener = () -> Unit
typealias OnAddReactionClickListener = () -> Unit

internal abstract class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes),
    ListUpdateCallback {

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
        const val DEFAULT_MAX_WIDTH_OF_PARENT = 0.85f

        private val diffUtilChatReactionCallback by lazy { ChatReactionDiffUtilCallback() }
    }

    private val asyncReactionsDiffer = AsyncListDiffer<ChatReaction>(this,
        AsyncDifferConfig.Builder(diffUtilChatReactionCallback).build())

    val reactions get() = asyncReactionsDiffer.currentList
    var onMessageLongClickListener: OnMessageLongClickListener? = null
    var onReactionClickListener: OnReactionClickListener? = null
    var onAddReactionClickListener: OnAddReactionClickListener? = null
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
            messageTextView.text =
                HtmlCompat.fromHtml(value.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
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

    fun updateReactionsWithAsyncDiffUtil(newList: List<ChatReaction>?) {
        asyncReactionsDiffer.submitList(newList)
    }

    override fun onInserted(position: Int, count: Int) {
        for (i in position until position + count) {
            reactionsContainer.addView(createReactionView(reactions[i]), i)
        }
        addOrRemoveAddReactionButton(
            oldCount = reactions.size - count,
            newCount = reactions.size,
        )
        requestLayout()
    }

    override fun onRemoved(position: Int, count: Int) {
        for (i in position until position + count) {
            reactionsContainer.removeViewAt(position)
        }
        addOrRemoveAddReactionButton(
            oldCount = reactions.size + count,
            newCount = reactions.size,
        )
        requestLayout()
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        onChangedWithoutRequestLayout(position, count, payload)
        requestLayout()
    }

    private fun onChangedWithoutRequestLayout(position: Int, count: Int, payload: Any?) {
        (payload as? ChatReaction.Payload)?.let {
            for (i in position until position + count) {
                handlePayload(i, it)
            }
        } ?: run {
            for (i in position until position + count) {
                updateReactionView(getReactionViewByCode(reactions[i].emojiCode), reactions[i])
            }
        }
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        val leftPos = min(fromPosition, toPosition)
        val rightPos = max(fromPosition, toPosition)

        val leftView = reactionsContainer.getChildAt(leftPos)
        val rightView = reactionsContainer.getChildAt(rightPos)

        reactionsContainer.removeViewAt(rightPos)
        reactionsContainer.removeViewAt(leftPos)

        reactionsContainer.addView(rightView, leftPos)
        reactionsContainer.addView(leftView, rightPos)

        /* By some reason in some cases onChanged is called before onMoved so that's
        why this is required
         */
        onChangedWithoutRequestLayout(fromPosition, 1, null)
        onChangedWithoutRequestLayout(toPosition, 1, null)
    }

    private fun getReactionViewByCode(emojiCode: String): ReactionView {
        return (reactionsContainer.children.filterIsInstance<ReactionView>().firstOrNull { it.emojiCode == emojiCode })
            ?: error("Reaction with emojiCode $emojiCode is not found in msg $this ")
    }

    private fun updateReactionView(view: ReactionView, reaction: ChatReaction): ReactionView {
        return updateReactionView(view, reaction.emojiCode, reaction.count, reaction.userReacted)
    }

    private fun updateReactionView(
        view: ReactionView,
        emojiCode: String,
        count: Int,
        userReacted: Boolean,
    ): ReactionView {
        view.isSelected = userReacted
        view.count = count
        view.emojiCode = emojiCode
        view.setOnClickListener {
            onReactionClickListener?.invoke(count, emojiCode, userReacted)
        }
        return view
    }

    private fun addOrRemoveAddReactionButton(
        oldCount: Int,
        newCount: Int,
    ) {
        val buttonAlreadyExists = oldCount > 0
        val buttonShouldExist = newCount > 0

        if (buttonAlreadyExists && !buttonShouldExist
                && (reactionsContainer.children.lastOrNull() as? ReactionView)?.isPlus == true) {
            reactionsContainer.removeViewAt(reactionsContainer.childCount - 1)
        } else if (!buttonAlreadyExists && buttonShouldExist
                && (reactionsContainer.children.lastOrNull() as? ReactionView)?.isPlus == false) {
            reactionsContainer.addView(createAddReactionButtonView())
        }
    }

    private fun createAddReactionButtonView(): ReactionView {
        return ItemReactionBinding.inflate(
            layoutInflater,
            reactionsContainer,
            false
        ).root
            .also {
                it.isPlus = true
                it.setOnClickListener {
                    onAddReactionClickListener?.invoke()
                }
            }
    }

    private fun createReactionView(chatReaction: ChatReaction): ReactionView {
        return ItemReactionBinding.inflate(
            layoutInflater,
            reactionsContainer,
            false
        ).root
            .also {
                updateReactionView(it, chatReaction)
            }
    }

    private fun handlePayload(index: Int, payload: ChatReaction.Payload) {
        getReactionViewByCode(reactions[index].emojiCode).let { reactionView ->
            when (payload) {
                is ChatReaction.Payload.ChangedCount -> reactionView.count = payload.newCount
                is ChatReaction.Payload.ChangedUserReacted -> reactionView.isSelected =
                    payload.newUserReacted

                is ChatReaction.Payload.ChangedUserReactedAndCount -> {
                    reactionView.count = payload.newCount
                    reactionView.isSelected = payload.newUserReacted
                }
            }
        }
    }

    private class ChatReactionDiffUtilCallback : DiffUtil.ItemCallback<ChatReaction>() {
        override fun areItemsTheSame(oldItem: ChatReaction, newItem: ChatReaction): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ChatReaction, newItem: ChatReaction): Boolean {
            return oldItem == newItem
        }
        override fun getChangePayload(oldItem: ChatReaction, newItem: ChatReaction): Any? {
            return newItem.getPayload(oldItem)
        }
    }
}