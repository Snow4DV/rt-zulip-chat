package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.presentation.adapter.DateSplitterAdapterDelegate
import ru.snowadv.chat.presentation.adapter.IncomingMessageAdapterDelegate
import ru.snowadv.chat.presentation.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat.presentation.chat.event.ChatScreenEvent
import ru.snowadv.chat.presentation.chat.event.ChatScreenFragmentEvent
import ru.snowadv.chat.presentation.chat.state.ChatScreenState
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.chat.presentation.emoji_chooser.EmojiChooserBottomSheetDialog
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.util.FragmentDataObserver
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter

internal class ChatFragmentDataObserver :
    FragmentDataObserver<FragmentChatBinding, ChatViewModel, ChatFragment> {

    private lateinit var adapter: DiffDelegationAdapter
    private lateinit var splitterDateFormatter: DateFormatter
    private lateinit var dateTimeFormatter: DateTimeFormatter

    override fun ChatFragment.registerObservingFragment(
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
        observeState(binding, viewModel, this)
        initListeners(binding, viewModel)
        observeEventFlow(viewModel, this, binding)
    }

    private fun setAdapterToRecyclerViewWithLinearLayoutManager(
        context: Context,
        recyclerView: RecyclerView,
        adapter: DiffDelegationAdapter
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeState(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
        fragment: Fragment
    ) {
        viewModel.state.onEach {
            bindState(it, binding)
        }.flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle)
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEventFlow(viewModel: ChatViewModel, fragment: Fragment, binding: FragmentChatBinding) {
        viewModel.fragmentEventFlow
            .onEach {
                handleFragmentEventFlow(it, fragment, viewModel, binding)
            }
            .flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle)
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun initListeners(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
    ) {
        binding.bottomBar.messageEditText.addTextChangedListener {
            it?.toString()?.let { currentMessage ->
                if (viewModel.state.value.messageField != currentMessage) {
                    viewModel.event(ChatScreenEvent.TextFieldMessageChanged(currentMessage))
                }
            }
        }
        binding.bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            if (viewModel.state.value.messageField.isEmpty()) {
                viewModel.event(ChatScreenEvent.AddAttachmentButtonClicked)
            } else {
                viewModel.event(ChatScreenEvent.SendButtonClicked(binding.bottomBar.messageEditText.text.toString()))
            }
        }
    }

    private fun handleFragmentEventFlow(
        event: ChatScreenFragmentEvent,
        fragment: Fragment,
        viewModel: ChatViewModel,
        binding: FragmentChatBinding
    ) {
        when (event) {
            is ChatScreenFragmentEvent.OpenReactionChooser -> {
                openReactionChooser(event.destMessageId, fragment, viewModel)
            }

            is ChatScreenFragmentEvent.ExplainError -> {
                showErrorToast(fragment, R.string.unexpected_error)
            }
            
            is ChatScreenFragmentEvent.ExplainNotImplemented -> {
                showErrorToast(fragment, R.string.not_supported)
            }
            
            is ChatScreenFragmentEvent.ExplainReactionAlreadyExists -> {
                showErrorToast(fragment, R.string.reaction_already_exists)
            }

            is ChatScreenFragmentEvent.ScrollRecyclerToTheEnd -> {
                binding.messagesRecycler.scrollToPosition(adapter.itemCount - 1) // TODO: implement smooth scroll
            }
        }
    }

    private fun bindState(
        state: ChatScreenState,
        binding: FragmentChatBinding
    ) {
        binding.topicName.text = state.topic
        binding.topBar.barTitle.text = state.stream
        binding.bottomBar.sendOrAddAttachmentButton.setImageResource(
            if (state.messageField.isEmpty()) R.drawable.ic_add_attachment else R.drawable.ic_send
        )
        adapter.submitList(state.messagesAndDates)
        with(binding.bottomBar.messageEditText.text.toString()) {
            if (state.messageField != this) {
                binding.bottomBar.messageEditText.setText(state.messageField)
            }
        }
        binding.loadingBar.visibility = if (state.loading) View.VISIBLE else View.GONE
        binding.actionProgressBar.visibility = if (state.actionInProcess) View.VISIBLE else View.GONE
    }


    private fun initOutgoingMessagesDelegate(viewModel: ChatViewModel): OutgoingMessageAdapterDelegate {
        return OutgoingMessageAdapterDelegate(
            onLongMessageClickListener = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.event(ChatScreenEvent.RemoveReaction(message.id, reaction.name))
                } else {
                    viewModel.event(ChatScreenEvent.AddChosenReaction(message.id, reaction.name))
                }
            },
            onAddReactionClickListener = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            timestampFormatter = dateTimeFormatter,
        )
    }

    private fun initIncomingMessagesDelegate(viewModel: ChatViewModel): IncomingMessageAdapterDelegate {
        return IncomingMessageAdapterDelegate(
            onLongMessageClickListener = {
                viewModel.event(ChatScreenEvent.AddReactionClicked(it.id))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.event(ChatScreenEvent.RemoveReaction(message.id, reaction.name))
                } else {
                    viewModel.event(ChatScreenEvent.AddChosenReaction(message.id, reaction.name))
                }
            },
            timestampFormatter = dateTimeFormatter,
            onAddReactionClickListener = {
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

    private fun openReactionChooser(messageId: Long, fragment: Fragment, viewModel: ChatViewModel) {
        val dialog =
            EmojiChooserBottomSheetDialog.newInstance(
                listener = { chosenEmoji ->
                    viewModel.event(ChatScreenEvent.AddChosenReaction(messageId, chosenEmoji.name))
                }
            )
        dialog.show(fragment.childFragmentManager)
    }

    private fun showErrorToast(fragment: Fragment, stringResId: Int) {
        Toast.makeText(fragment.requireContext(), stringResId, Toast.LENGTH_LONG)
            .show()
    }

    private fun initDelegateAdapter(viewModel: ChatViewModel): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager(viewModel))
    }

    private fun initDateTimeFormatters(context: Context) { // TODO: Replace with DI
        dateTimeFormatter = LocalizedDateTimeFormatter(context)
        splitterDateFormatter = DayDateFormatter(context)
    }
}