package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.combine
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
import ru.snowadv.chat.presentation.model.ChatAction
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.fragment.setTopBarText
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter
import ru.snowadv.presentation.view.setTextIfChanged
import kotlin.math.abs

internal class ChatFragmentDataObserver :
    FragmentDataObserver<FragmentChatBinding, ChatViewModel, ChatFragment> {

    private var _adapter: DiffDelegationAdapter? = null
    private var _splitterDateFormatter: DateFormatter? = null
    private var _dateTimeFormatter: DateTimeFormatter? = null

    private val adapter get() = requireNotNull(_adapter) {"Adapter wasn't initialized"}
    private val splitterDateFormatter get() = requireNotNull(_splitterDateFormatter) {"Splitter date formatter wasnt initialized"}
    private val dateTimeFormatter get() = requireNotNull(_dateTimeFormatter) {"Date time formatter wasn't initialized"}

    companion object {
        const val EMOJI_CHOOSER_REQUEST_KEY = "emoji_chooser_request"
    }

    override fun ChatFragment.registerObservingFragment(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
    ) {
        initDateTimeFormatters(requireContext())
        initEmojiChooserResultListener(this, viewModel)
        _adapter = initDelegateAdapter(viewModel).also {
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

    private fun Fragment.observeState(
        binding: FragmentChatBinding,
        viewModel: ChatViewModel,
        fragment: Fragment
    ) {
        viewModel.state.onEach { state ->
            render(state, binding)
        }.flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle)
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEventFlow(
        viewModel: ChatViewModel,
        fragment: ChatFragment,
        binding: FragmentChatBinding
    ) {
        viewModel.eventFlow
            .onEach {
                fragment.handleFragmentEventFlow(it, viewModel, binding)
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
                viewModel.handleEvent(ChatScreenEvent.MessageFieldChanged(currentMessage))
            }
        }
        binding.bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            viewModel.handleEvent(ChatScreenEvent.SendMessageAddAttachmentButtonClicked)
        }
        binding.topBackButtonBar.backButton.setOnClickListener {
            viewModel.handleEvent(ChatScreenEvent.GoBackClicked)
        }
        binding.stateBox.setOnRetryClickListener {
            viewModel.handleEvent(ChatScreenEvent.ReloadClicked)
        }
    }

    private fun ChatFragment.handleFragmentEventFlow(
        event: ChatScreenFragmentEvent,
        viewModel: ChatViewModel,
        binding: FragmentChatBinding
    ) {
        when (event) {
            is ChatScreenFragmentEvent.OpenReactionChooser -> {
                openReactionChooser(event.destMessageId, this)
            }

            is ChatScreenFragmentEvent.ExplainNotImplemented -> {
                showErrorToast(this, R.string.not_supported)
            }

            is ChatScreenFragmentEvent.ExplainReactionAlreadyExists -> {
                showErrorToast(this, R.string.reaction_already_exists)
            }

            is ChatScreenFragmentEvent.ScrollRecyclerToTheEnd -> {
                binding.messagesRecycler.smoothScrollToPosition(abs(adapter.itemCount - 1))
            }

            is ChatScreenFragmentEvent.OpenMessageActionsChooser -> {
                openActionsDialog(ChatAction.createActionsForMessage(event.messageId, event.userId)) {
                    when(it) {
                        is ChatAction.AddReaction -> viewModel.handleEvent(ChatScreenEvent.AddReactionClicked(event.messageId))
                        is ChatAction.OpenProfile -> viewModel.handleEvent(ChatScreenEvent.GoToProfileClicked(event.userId))
                    }
                }
            }

            is ChatScreenFragmentEvent.ShowInternetErrorWithRetry -> {
                showActionInternetErrorWithRetry(binding.root) {
                    event.retryAction()
                }
            }
        }
    }

    private fun Fragment.render(
        state: ChatScreenState,
        binding: FragmentChatBinding
    ) = with (binding) {
        topicName.text = getString(ru.snowadv.presentation.R.string.topic_title, state.topic)
        topBackButtonBar.setTopBarText(getString(ru.snowadv.presentation.R.string.stream_title, state.stream))
        bottomBar.sendOrAddAttachmentButton.setImageResource(
            when(state.actionButtonType) {
                ChatScreenState.ActionButtonType.SEND_MESSAGE -> R.drawable.ic_send
                ChatScreenState.ActionButtonType.ADD_ATTACHMENT -> R.drawable.ic_add_attachment
            }
        )
        bottomBar.sendOrAddAttachmentButton.isVisible = state.isActionButtonVisible
        adapter.submitList(state.screenState.getCurrentData()) {
            if (adapter.itemCount > 0) {
                binding.messagesRecycler.scrollToPosition(adapter.itemCount - 1)
            }
        }
        binding.bottomBar.messageEditText.setTextIfChanged(state.messageField)
        stateBox.inflateState(state.screenState, R.layout.fragment_chat_shimmer)
        actionProgressBar.isVisible = state.changingReaction || state.sendingMessage
    }


    private fun initOutgoingMessagesDelegate(viewModel: ChatViewModel): OutgoingMessageAdapterDelegate {
        return OutgoingMessageAdapterDelegate(
            onLongMessageClickListener = {
                viewModel.handleEvent(ChatScreenEvent.MessageLongClicked(it.id, it.senderId))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.handleEvent(ChatScreenEvent.RemoveReaction(message.id, reaction.name))
                } else {
                    viewModel.handleEvent(ChatScreenEvent.AddChosenReaction(message.id, reaction.name))
                }
            },
            onAddReactionClickListener = {
                viewModel.handleEvent(ChatScreenEvent.AddReactionClicked(it.id))
            },
            timestampFormatter = dateTimeFormatter,
        )
    }

    private fun initIncomingMessagesDelegate(viewModel: ChatViewModel): IncomingMessageAdapterDelegate {
        return IncomingMessageAdapterDelegate(
            onLongMessageClickListener = {
                viewModel.handleEvent(ChatScreenEvent.MessageLongClicked(it.id, it.senderId))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    viewModel.handleEvent(ChatScreenEvent.RemoveReaction(message.id, reaction.name))
                } else {
                    viewModel.handleEvent(ChatScreenEvent.AddChosenReaction(message.id, reaction.name))
                }
            },
            timestampFormatter = dateTimeFormatter,
            onAddReactionClickListener = {
                viewModel.handleEvent(ChatScreenEvent.AddReactionClicked(it.id))
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

    private fun openReactionChooser(messageId: Long, fragment: Fragment) {
        val dialog = EmojiChooserBottomSheetDialog.newInstance(EMOJI_CHOOSER_REQUEST_KEY, messageId)
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
        _dateTimeFormatter = LocalizedDateTimeFormatter(context)
        _splitterDateFormatter = DayDateFormatter(context)
    }

    private fun initEmojiChooserResultListener(fragment: Fragment, viewModel: ChatViewModel) {
        fragment.childFragmentManager.setFragmentResultListener(EMOJI_CHOOSER_REQUEST_KEY, fragment.viewLifecycleOwner) { key, bundle ->
            val chosenReaction =
                bundle.getString(EmojiChooserBottomSheetDialog.BUNDLE_CHOSEN_REACTION_NAME)
                    ?: error("No reaction came as result from EmojiChooserBottomSheetDialog")
            val messageId = bundle.getLong(
                EmojiChooserBottomSheetDialog.BUNDLE_MESSAGE_ID_KEY,
                EmojiChooserBottomSheetDialog.DEFAULT_ARG_MESSAGE_ID
            )
            if (messageId == EmojiChooserBottomSheetDialog.DEFAULT_ARG_MESSAGE_ID) {
                error("Missing messageId in result bundle from emoji chooser")
            }
            viewModel.handleEvent(ChatScreenEvent.AddChosenReaction(messageId, chosenReaction))
        }
    }

    override fun ChatFragment.onViewDestroyedToObserver() {
        _adapter = null
        _dateTimeFormatter = null
        _splitterDateFormatter = null
    }
}