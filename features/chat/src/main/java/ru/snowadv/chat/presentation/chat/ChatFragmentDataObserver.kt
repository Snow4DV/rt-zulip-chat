package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.domain.model.emojiMap
import ru.snowadv.chat.presentation.adapter.DateSplitterAdapterDelegate
import ru.snowadv.chat.presentation.adapter.IncomingMessageAdapterDelegate
import ru.snowadv.chat.presentation.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat.presentation.chat.event.ChatScreenEvent
import ru.snowadv.chat.presentation.chat.state.ChatScreenState
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.chat.presentation.emoji_chooser.EmojiChooserBottomSheetDialog
import ru.snowadv.chat.presentation.util.toUiChatEmoji
import ru.snowadv.presentation.adapter.AdapterDelegate
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.util.FragmentDataObserver
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter

internal class ChatFragmentDataObserver : FragmentDataObserver<FragmentChatBinding, ChatViewModel> {

    private lateinit var adapter: DiffDelegationAdapter
    private lateinit var splitterDateFormatter: DateFormatter
    private lateinit var dateTimeFormatter: DateTimeFormatter

    override fun Fragment.registerObservingFragment(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
    ) {
        initDateTimeFormatters(requireContext())
        adapter = initDelegateAdapter(viewModel).also {
            setAdapterToRecyclerViewWithLinearLayoutManager(
                requireContext(),
                binding.messagesRecycler,
                it
            )
        }
        subscribeToState(binding, viewModel, this)
        initListeners(binding, viewModel)
    }

    private fun setAdapterToRecyclerViewWithLinearLayoutManager(
        context: Context,
        recyclerView: RecyclerView,
        adapter: DiffDelegationAdapter
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeToState(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
        fragment: Fragment
    ) {
        viewModel.state.onEach {
            bindState(it, binding, fragment)
        }.flowWithLifecycle(fragment.lifecycle).launchIn(fragment.lifecycleScope)
    }

    private fun initListeners(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
    ) {
        binding.bottomBar.messageEditText.addTextChangedListener {
            it?.toString()?.let { currentMessage ->
                if(viewModel.state.value.messageField != currentMessage) {
                    viewModel.event(ChatScreenEvent.TextFieldMessageChanged(currentMessage))
                }
            }
        }
        binding.bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            viewModel.event(ChatScreenEvent.SendButtonClicked(binding.bottomBar.messageEditText.text.toString()))
        }
    }

    private fun bindState(
        state: ChatScreenState,
        binding: FragmentChatBinding,
        fragment: Fragment
    ) {
        binding.topicName.text = state.topic
        binding.topBar.barTitle.text = state.stream
        binding.bottomBar.sendOrAddAttachmentButton.setImageDrawable( // TODO: cache previous state
            ResourcesCompat.getDrawable(
                fragment.resources,
                if (state.messageField.isEmpty()) R.drawable.ic_add_attachment else R.drawable.ic_send,
                fragment.context?.theme,
            )
        )
        adapter.submitList(state.messagesAndDates)
        binding.loadingBar.visibility = if (state.loading) View.VISIBLE else View.GONE
        with(binding.bottomBar.messageEditText.text.toString()) {
            if (state.messageField != this) {
                binding.bottomBar.messageEditText.setText(state.messageField)
            }
        }
    }


    private fun initOutgoingMessagesDelegate(viewModel: ChatViewModel): OutgoingMessageAdapterDelegate {
        return OutgoingMessageAdapterDelegate(
            onLongMessageClick = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            onReactionClick = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.event(ChatScreenEvent.RemoveReaction(message.id, reaction.emoji))
                } else {
                    viewModel.event(ChatScreenEvent.AddChosenReaction(message.id, reaction.emoji))
                }
            },
            onAddReactionClick = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            timestampFormatter = dateTimeFormatter,
        )
    }

    private fun initIncomingMessagesDelegate(viewModel: ChatViewModel): IncomingMessageAdapterDelegate {
        return IncomingMessageAdapterDelegate(
            onLongMessageClick = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            onReactionClick = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.event(ChatScreenEvent.RemoveReaction(message.id, reaction.emoji))
                } else {
                    viewModel.event(ChatScreenEvent.AddChosenReaction(message.id, reaction.emoji))
                }
            },
            timestampFormatter = dateTimeFormatter,
            onAddReactionClick = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
        )
    }

    private fun initDateSplitterDelegate(): DateSplitterAdapterDelegate {
        return DateSplitterAdapterDelegate(splitterDateFormatter)
    }


    private fun initDelegatesManager(viewModel: ChatViewModel): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            initDateSplitterDelegate(),
            initIncomingMessagesDelegate(viewModel),
            initOutgoingMessagesDelegate(viewModel)
        )
    }

    private fun openEmojiChooser(messageId: Long, fragment: Fragment, viewModel: ChatViewModel) {
        val dialog =
            EmojiChooserBottomSheetDialog.newInstance(
                emojis = emojiMap.values.map { it.toUiChatEmoji() },
                listener = { chosenEmoji ->
                    viewModel.event(ChatScreenEvent.AddChosenReaction(messageId, chosenEmoji))
                }
            )
        dialog.show(fragment.childFragmentManager)
    }

    private fun initDelegateAdapter(viewModel: ChatViewModel): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager(viewModel))
    }

    private fun initDateTimeFormatters(context: Context) { // TODO: Replace with DI
        dateTimeFormatter = LocalizedDateTimeFormatter(context)
        splitterDateFormatter = DayDateFormatter(context)
    }
}