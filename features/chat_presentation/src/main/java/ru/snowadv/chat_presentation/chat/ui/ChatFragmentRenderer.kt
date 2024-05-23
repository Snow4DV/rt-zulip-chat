package ru.snowadv.chat_presentation.chat.ui

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.noties.markwon.Markwon
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.adapter.IncomingMessageAdapterDelegate
import ru.snowadv.chat_presentation.chat.ui.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat_presentation.chat.ui.adapter.PaginationStatusAdapterDelegate
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.ui.adapter.DateSplitterAdapterDelegate
import ru.snowadv.chat_presentation.chat.ui.adapter.TopicAdapterDelegate
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEventUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatStateUiElm
import ru.snowadv.chat_presentation.databinding.FragmentChatBinding
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.chat_presentation.emoji_chooser.ui.EmojiChooserBottomSheetDialog
import ru.snowadv.chat_presentation.chat.ui.model.ChatAction
import ru.snowadv.chat_presentation.chat.ui.model.ChatPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils.submitListAndKeepScrolledToBottom
import ru.snowadv.chat_presentation.chat.ui.util.PaginationConfig
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ChatFragmentRenderer :
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized" }

    private var _messagesRecyclerLinearLayoutManager: LinearLayoutManager? = null
    private val messagesRecyclerLinearLayoutManager: LinearLayoutManager
        get() =
            requireNotNull(_messagesRecyclerLinearLayoutManager) { "Linear Layout Manager wasn't initialized for messages recycler" }

    @Inject
    internal lateinit var splitterDateFormatter: DateFormatter

    @Inject
    internal lateinit var dateTimeFormatter: DateTimeFormatter

    @Inject
    internal lateinit var markwon: Markwon

    @Inject
    internal lateinit var mapper: ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm>

    companion object {
        const val EMOJI_CHOOSER_REQUEST_KEY = "emoji_chooser_request"
    }

    override fun ChatFragment.onAttachRendererView() {
        ChatPresentationComponentHolder.getComponent().inject(this@ChatFragmentRenderer)
    }

    override fun ChatFragment.onRendererViewCreated(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>
    ) {
        initEmojiChooserResultListener(this, store)
        _adapter = initDelegateAdapter(store).also {
            setAdapterToRecyclerView(binding.messagesRecycler, it)
        }
        _messagesRecyclerLinearLayoutManager =
            binding.messagesRecycler.layoutManager as? LinearLayoutManager
                ?: error("Wrong LinearLayoutManager is set to messages recycler")
        initListeners(binding, store)
    }

    override fun ChatFragment.renderStateByRenderer(
        state: ChatStateElm,
        binding: FragmentChatBinding
    ) = with(binding) {
        val mappedState = mapper.mapState(state)

        topicName.isVisible = mappedState.topic != null
        topicName.text = getString(ru.snowadv.presentation.R.string.topic_title, mappedState.topic)
        chatTopBar.barTitle.text = getString(
            ru.snowadv.presentation.R.string.stream_title,
            mappedState.stream,
        )
        chatTopBar.openAllTopicsButton.isVisible = mappedState.topic != null
        bottomBar.sendOrAddAttachmentButton.setImageResource(mappedState.actionButtonType.buttonResId)
        bottomBar.sendOrAddAttachmentButton.isVisible = mappedState.isActionButtonVisible
        bottomBar.sendOrAddAttachmentButton.contentDescription =
            getString(mappedState.actionButtonType.hintTextResId)
        bottomBar.sendOrAddAttachmentButton.tag =
            getString(mappedState.actionButtonType.hintTextResId)

        adapter.submitListAndKeepScrolledToBottom(
            recycler = binding.messagesRecycler,
            list = listOf(mappedState.paginationStatus) + (mappedState.screenState.getCurrentData()
                ?: emptyList()),
        ) {
            stateBox.inflateState(
                mappedState.screenState,
                R.layout.fragment_chat_shimmer,
                topStateBox
            )
        }

        if (mappedState.messageField.isEmpty() || bottomBar.messageEditText.text.toString().isEmpty()) {
            bottomBar.messageEditText.setText(mappedState.messageField)
        }

        actionProgressBar.isVisible =
            mappedState.changingReaction || mappedState.sendingMessage || mappedState.uploadingFile
    }

    override fun ChatFragment.handleEffectByRenderer(
        effect: ChatEffectElm,
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) = with(mapper.mapEffect(effect)) {
        when (this) {
            is ChatEffectUiElm.OpenReactionChooser -> {
                openReactionChooser(destMessageId, this@handleEffectByRenderer)
            }

            is ChatEffectUiElm.OpenMessageActionsChooser -> {
                openActionsDialog(
                    ChatAction.createActionsForMessage(
                        messageId,
                        userId,

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

            is ChatEffectUiElm.ShowActionErrorWithRetry -> {
                showActionInternetErrorWithRetry(binding.root) {
                    store.accept(retryEvent)
                }
            }

            ChatEffectUiElm.ShowActionError -> {
                showErrorToast(
                    this@handleEffectByRenderer,
                    ru.snowadv.presentation.R.string.action_internet_error
                )
            }

            ChatEffectUiElm.OpenFileChooser -> openFilePicker()
        }
    }

    override fun ChatFragment.onDestroyRendererView() {
        _adapter = null
        _messagesRecyclerLinearLayoutManager = null
    }

    private fun setAdapterToRecyclerView(
        recyclerView: RecyclerView,
        adapter: DiffDelegationAdapter
    ) {
        recyclerView.adapter = adapter
    }

    private fun initListeners(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) {
        binding.chatTopBar.openAllTopicsButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.OnLeaveTopicClicked))
        }
        binding.bottomBar.messageEditText.addTextChangedListener {
            it?.toString()?.let { currentMessage ->
                store.accept(mapper.mapUiEvent(ChatEventUiElm.MessageFieldChanged(currentMessage)))
            }
        }
        binding.bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.SendMessageAddAttachmentButtonClicked))
        }
        binding.chatTopBar.backButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.GoBackClicked))
        }
        binding.stateBox.setOnRetryClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.ReloadClicked))
        }

        binding.messagesRecycler.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (messagesRecyclerLinearLayoutManager.findFirstVisibleItemPosition() < PaginationConfig.TOP_MESSAGES_TO_FETCH_COUNT
                && scrollY - oldScrollY < 0
            ) {
                store.accept(mapper.mapUiEvent(ChatEventUiElm.ScrolledToNTopMessages))
            }
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
            markwon = markwon,
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
            markwon = markwon,
        )
    }

    private fun initDateSplitterDelegate(): DateSplitterAdapterDelegate {
        return DateSplitterAdapterDelegate(
            splitterDateFormatter
        )
    }


    private fun initDelegatesManager(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            initDateSplitterDelegate(),
            initIncomingMessagesDelegate(store),
            initOutgoingMessagesDelegate(store),
            initPaginationStatusDelegate(store),
            initTopicDelegate(store),
        )
    }

    private fun initTopicDelegate(store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>): TopicAdapterDelegate {
        return TopicAdapterDelegate(
            onTopicClickListener = {
                store.accept(mapper.mapUiEvent(ChatEventUiElm.ClickedOnTopic(topicName = it.name)))
            },
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
            val messageId = bundle.getLong(
                EmojiChooserBottomSheetDialog.BUNDLE_MESSAGE_ID_KEY,
                EmojiChooserBottomSheetDialog.DEFAULT_ARG_MESSAGE_ID
            )
            if (messageId != EmojiChooserBottomSheetDialog.DEFAULT_ARG_MESSAGE_ID && chosenReaction != null) {
                store.accept(
                    ChatEventUiElm.AddChosenReaction(
                        messageId = messageId,
                        reactionName = chosenReaction,
                    ).let { mapper.mapUiEvent(it) }
                )
            }
        }
    }

}