package ru.snowadv.message_actions_presentation.action_chooser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.facebook.shimmer.Shimmer
import ru.snowadv.message_actions_presentation.action_chooser.ui.model.UiMessageAction
import ru.snowadv.message_actions_presentation.databinding.ItemEmojiBinding
import ru.snowadv.message_actions_presentation.databinding.ItemMessageActionBinding
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.adapter.OnEmojiItemClickListener
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal typealias OnActionItemClickListener = (UiMessageAction) -> Unit

internal class MessageActionAdapterDelegate(private val listener: OnActionItemClickListener? = null) :
    DelegationItemAdapterDelegate<UiMessageAction, MessageActionAdapterDelegate.MessageActionViewHolder, Nothing>() {

    companion object {
        private const val SHIMMER_DURATION = 1000L
        private const val SHIMMER_BASE_ALPHA = 0.2f
        private const val SHIMMER_HIGHLIGHT_ALPHA = 0.95f
        private const val SHIMMER_DIRECTION = Shimmer.Direction.LEFT_TO_RIGHT
        private const val DEFAULT_SHIMMER_AUTOSTART = true
    }

    internal inner class MessageActionViewHolder(private val binding: ItemMessageActionBinding) :
        ViewHolder(binding.root) {

        fun bind(action: UiMessageAction) = with(binding) {
            actionIcon.setImageResource(action.iconResId)
            actionNameText.text = root.context.getString(action.nameResId)
            bindLoading(action.loading)
        }

        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        listener?.invoke(it)
                    }
                }
            }
        }

        private fun bindLoading(loadingState: Boolean) = with(binding) {
            actionShimmer.setShimmer(if (loadingState) createDefaultShimmer() else null)
        }

        private fun createDefaultShimmer(): Shimmer {
            return Shimmer.AlphaHighlightBuilder()
                .setDuration(SHIMMER_DURATION)
                .setBaseAlpha(SHIMMER_BASE_ALPHA)
                .setHighlightAlpha(SHIMMER_HIGHLIGHT_ALPHA)
                .setDirection(SHIMMER_DIRECTION)
                .setAutoStart(DEFAULT_SHIMMER_AUTOSTART)
                .build()
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is UiMessageAction
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return MessageActionViewHolder(
            ItemMessageActionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { it.initClickListeners(getCurrentList) }
    }

    override fun onBindViewHolder(
        item: UiMessageAction,
        holder: MessageActionViewHolder,
        payloads: List<Nothing>
    ) {
        holder.bind(item)
    }
}