package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.presentation.adapter.DateSplitterAdapterDelegate
import ru.snowadv.chat.presentation.adapter.IncomingMessageAdapterDelegate
import ru.snowadv.chat.presentation.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat.presentation.adapter.PaginationStatusAdapterDelegate
import ru.snowadv.chat.presentation.chat.elm.ChatEffectElm
import ru.snowadv.chat.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat.presentation.chat.elm.ChatStateElm
import ru.snowadv.chat.presentation.emoji_chooser.EmojiChooserBottomSheetDialog
import ru.snowadv.chat.presentation.model.ChatAction
import ru.snowadv.chat.presentation.model.ChatPaginationStatus
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.fragment.setTopBarText
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter
import ru.snowadv.presentation.view.setTextIfEmpty
import vivid.money.elmslie.core.store.Store
import kotlin.math.abs

internal class ChatFragmentRenderer :
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private var _splitterDateFormatter: DateFormatter? = null
    private var _dateTimeFormatter: DateTimeFormatter? = null

    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized" }
    private val splitterDateFormatter get() = requireNotNull(_splitterDateFormatter) { "Splitter date formatter wasnt initialized" }
    private val dateTimeFormatter get() = requireNotNull(_dateTimeFormatter) { "Date time formatter wasn't initialized" }

    companion object {
        const val EMOJI_CHOOSER_REQUEST_KEY = "emoji_chooser_request"
    }

    override fun ChatFragment.onRendererViewCreated(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>
    ) {
        initDateTimeFormatters(requireContext())
        initEmojiChooserResultListener(this, store)
        _adapter = initDelegateAdapter(store).also {
            setAdapterToRecyclerView(
                requireContext(),
                binding.messagesRecycler,
                it
            )
        }
        initListeners(binding, store)
    }

    override fun ChatFragment.renderStateByRenderer(
        state: ChatStateElm,
        binding: FragmentChatBinding
    ) = with(binding) {
        topicName.text = getString(ru.snowadv.presentation.R.string.topic_title, state.topic)
        topBackButtonBar.setTopBarText(
            getString(
                ru.snowadv.presentation.R.string.stream_title,
                state.stream
            )
        )
        bottomBar.sendOrAddAttachmentButton.setImageResource(
            when (state.actionButtonType) {
                ChatStateElm.ActionButtonType.SEND_MESSAGE -> R.drawable.ic_send
                ChatStateElm.ActionButtonType.ADD_ATTACHMENT -> R.drawable.ic_add_attachment
            }
        )
        bottomBar.sendOrAddAttachmentButton.isVisible = state.isActionButtonVisible

        adapter.submitList(listOf(state.paginationStatus) + (state.screenState.getCurrentData() ?: emptyList())) {
            stateBox.inflateState(state.screenState, R.layout.fragment_chat_shimmer)
        }

        binding.bottomBar.messageEditText.setTextIfEmpty(state.messageField)

        actionProgressBar.isVisible = state.changingReaction || state.sendingMessage
    }

    override fun ChatFragment.handleEffectByRenderer(
        effect: ChatEffectElm,
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) {
        when (effect) {
            is ChatEffectElm.OpenReactionChooser -> {
                openReactionChooser(effect.destMessageId, this)
            }

            is ChatEffectElm.ExplainNotImplemented -> {
                showErrorToast(this, R.string.not_supported)
            }

            is ChatEffectElm.ExplainReactionAlreadyExists -> {
                showErrorToast(this, R.string.reaction_already_exists)
            }

            is ChatEffectElm.ScrollRecyclerToTheEnd -> {
                binding.messagesRecycler.smoothScrollToPosition(abs(adapter.itemCount - 1))
            }

            is ChatEffectElm.OpenMessageActionsChooser -> {
                openActionsDialog(
                    ChatAction.createActionsForMessage(
                        effect.messageId,
                        effect.userId
                    )
                ) {
                    when (it) {
                        is ChatAction.AddReaction -> {
                            store.accept(ChatEventElm.Ui.AddReactionClicked(it.messageId))
                        }

                        is ChatAction.OpenProfile -> {
                            store.accept(ChatEventElm.Ui.GoToProfileClicked(it.userId))
                        }
                    }
                }
            }

            is ChatEffectElm.ShowActionErrorWithRetry -> {
                showActionInternetErrorWithRetry(binding.root) {
                    store.accept(effect.retryEvent)
                }
            }

            ChatEffectElm.ShowActionError -> {
                showErrorToast(this, ru.snowadv.presentation.R.string.action_internet_error)
            }
        }
    }

    override fun ChatFragment.onDestroyRendererView() {
        _adapter = null
        _dateTimeFormatter = null
        _splitterDateFormatter = null
    }

    private fun setAdapterToRecyclerView(
        context: Context,
        recyclerView: RecyclerView,
        adapter: DiffDelegationAdapter
    ) {
        recyclerView.adapter = adapter
    }
    private fun initListeners(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) {
        binding.bottomBar.messageEditText.addTextChangedListener {
            it?.toString()?.let { currentMessage ->
                store.accept(ChatEventElm.Ui.MessageFieldChanged(currentMessage))
            }
        }
        binding.bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            store.accept(ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked)
        }
        binding.topBackButtonBar.backButton.setOnClickListener {
            store.accept(ChatEventElm.Ui.GoBackClicked)
        }
        binding.stateBox.setOnRetryClickListener {
            store.accept(ChatEventElm.Ui.ReloadClicked)
        }
        binding.messagesRecycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

        }
    }

    private fun initPaginationStatusDelegate(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): PaginationStatusAdapterDelegate {
        return PaginationStatusAdapterDelegate(
            onPaginationStatusClick = {
                if (it is ChatPaginationStatus.HasMore || it is ChatPaginationStatus.Error) {
                    store.accept(ChatEventElm.Ui.PaginationLoadMore)
                }
            }
        )
    }

    private fun initOutgoingMessagesDelegate(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): OutgoingMessageAdapterDelegate {
        return OutgoingMessageAdapterDelegate(
            onLongMessageClickListener = {
                store.accept(ChatEventElm.Ui.MessageLongClicked(it.id, it.senderId))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    store.accept(ChatEventElm.Ui.RemoveReaction(message.id, reaction.name))
                } else {
                    store.accept(
                        ChatEventElm.Ui.AddChosenReaction(
                            messageId = message.id,
                            reactionName = reaction.name,
                        )
                    )
                }
            },
            onAddReactionClickListener = {
                store.accept(ChatEventElm.Ui.AddReactionClicked(it.id))
            },
            timestampFormatter = dateTimeFormatter,
        )
    }

    private fun initIncomingMessagesDelegate(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): IncomingMessageAdapterDelegate {
        return IncomingMessageAdapterDelegate(
            onLongMessageClickListener = {
                store.accept(ChatEventElm.Ui.MessageLongClicked(it.id, it.senderId))
            },
            onReactionClickListener = { reaction, message ->
                if (reaction.userReacted) {
                    store.accept(ChatEventElm.Ui.RemoveReaction(message.id, reaction.name))
                } else {
                    store.accept(
                        ChatEventElm.Ui.AddChosenReaction(
                            messageId = message.id,
                            reactionName = reaction.name,
                        )
                    )
                }
            },
            timestampFormatter = dateTimeFormatter,
            onAddReactionClickListener = {
                store.accept(ChatEventElm.Ui.AddReactionClicked(it.id))
            },
        )
    }

    private fun initDateSplitterDelegate(): DateSplitterAdapterDelegate {
        return DateSplitterAdapterDelegate(splitterDateFormatter)
    }


    private fun initDelegatesManager(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            initDateSplitterDelegate(),
            initIncomingMessagesDelegate(store),
            initOutgoingMessagesDelegate(store),
            initPaginationStatusDelegate(store),
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

    private fun initDelegateAdapter(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager(store))
    }

    private fun initDateTimeFormatters(context: Context) {
        _dateTimeFormatter = LocalizedDateTimeFormatter(context)
        _splitterDateFormatter = DayDateFormatter(context)
    }

    private fun initEmojiChooserResultListener(
        fragment: Fragment,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) {
        fragment.childFragmentManager.setFragmentResultListener(
            EMOJI_CHOOSER_REQUEST_KEY,
            fragment.viewLifecycleOwner
        ) { _, bundle ->
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
            store.accept(ChatEventElm.Ui.AddChosenReaction(
                messageId = messageId,
                reactionName = chosenReaction,
            ))
        }
    }

}