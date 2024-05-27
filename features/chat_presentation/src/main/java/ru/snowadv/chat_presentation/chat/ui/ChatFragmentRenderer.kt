package ru.snowadv.chat_presentation.chat.ui

import android.widget.Toast
import androidx.core.view.isVisible
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
import android.widget.ArrayAdapter;
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult
import ru.snowadv.message_actions_presentation.api.model.EmojiChooserResult
import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.message_actions_presentation.api.model.MessageMoveResult
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.EmojiChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageEditorDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageTopicChangerDialogFactory
import ru.snowadv.presentation.adapter.updateIfChanged
import ru.snowadv.presentation.fragment.getParcelableTypeSafe
import ru.snowadv.presentation.view.EditTextUtils.observe
import ru.snowadv.presentation.view.setTextIfChanged
import javax.inject.Inject

internal class ChatFragmentRenderer :
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized" }

    private var _messagesRecyclerLinearLayoutManager: LinearLayoutManager? = null
    private val messagesRecyclerLinearLayoutManager: LinearLayoutManager
        get() =
            requireNotNull(_messagesRecyclerLinearLayoutManager) { "Linear Layout Manager wasn't initialized for messages recycler" }

    private var _topicsAdapter: ArrayAdapter<String>? = null
    private val topicsAdapter get() = requireNotNull(_topicsAdapter) { "Topics adapter wasn't initialized" }

    @Inject
    internal lateinit var splitterDateFormatter: DateFormatter

    @Inject
    internal lateinit var dateTimeFormatter: DateTimeFormatter

    @Inject
    internal lateinit var markwon: Markwon

    @Inject
    internal lateinit var mapper: ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm>

    @Inject
    internal lateinit var emojiChooserFactory: EmojiChooserDialogFactory
    @Inject
    internal lateinit var actionChooserDialogFactory: ActionChooserDialogFactory
    @Inject
    internal lateinit var messageEditorDialogFactory: MessageEditorDialogFactory
    @Inject
    internal lateinit var messageTopicChangerDialogFactory: MessageTopicChangerDialogFactory

    companion object {
        const val EMOJI_CHOOSER_REQUEST_KEY = "emoji_chooser_request"
        const val ACTION_CHOOSER_REQUEST_KEY ="action_chooser_request"
        const val MESSAGE_EDIT_REQUEST_KEY = "message_edit_request"
        const val MESSAGE_MOVE_REQUEST_KEY = "message_move_request"
    }

    override fun ChatFragment.onAttachRendererView() {
        ChatPresentationComponentHolder.getComponent().inject(this@ChatFragmentRenderer)
    }

    override fun ChatFragment.onRendererViewCreated(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>
    ) {
        initEmojiChooserResultListener(this, store)
        initActionChooserResultListener(store, binding)
        initMessageEditorResultListener(binding)
        initMessageTopicChangerResultListener(binding)
        _adapter = initDelegateAdapter(store).also {
            setAdapterToRecyclerView(binding.messagesRecycler, it)
        }
        _messagesRecyclerLinearLayoutManager =
            binding.messagesRecycler.layoutManager as? LinearLayoutManager
                ?: error("Wrong LinearLayoutManager is set to messages recycler")
        _topicsAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_dropdown_item_1line)
        binding.bottomBar.topicAutoCompleteTextView.setAdapter(topicsAdapter)
        initListeners(binding, store)
    }

    override fun ChatFragment.renderStateByRenderer(
        state: ChatStateElm,
        binding: FragmentChatBinding
    ) = with(binding) {
        val mappedState = mapper.mapState(state)

        bottomBar.topicAutoCompleteTextView.setTextIfChanged(text = mappedState.sendTopic, moveCursorToEnd = true)
        bottomBar.topicInputLayout.error = if (mappedState.isTopicEmptyErrorVisible) {
            getString(R.string.topic_cant_be_empty)
        } else {
            null
        }
        bottomBar.topicInputLayout.isErrorEnabled = mappedState.isTopicEmptyErrorVisible
        bottomBar.topicInputLayout.isVisible = mappedState.isTopicChooserVisible
        if (!mappedState.isTopicChooserVisible) bottomBar.topicInputLayout.clearFocus()
        topicName.isVisible = mappedState.topic != null
        topicName.text = getString(ru.snowadv.presentation.R.string.topic_title, mappedState.topic)
        chatTopBar.barTitle.text = getString(
            ru.snowadv.presentation.R.string.stream_title,
            mappedState.stream,
        )
        chatTopBar.openAllTopicsButton.isVisible = mappedState.topic != null
        bottomBar.sendOrAddAttachmentButton.setImageResource(mappedState.actionButtonType.buttonResId)
        bottomBar.sendOrAddAttachmentButton.isVisible = !mappedState.isLoading
        bottomBar.sendOrAddAttachmentButton.contentDescription =
            getString(mappedState.actionButtonType.hintTextResId)
        bottomBar.sendOrAddAttachmentButton.tag =
            getString(mappedState.actionButtonType.hintTextResId)

        adapter.submitListAndKeepScrolledToBottom(
            recycler = messagesRecycler,
            list = listOf(mappedState.paginationStatus) + (mappedState.screenState.getCurrentData()
                ?: emptyList()),
        ) {
            stateBox.inflateState(
                screenState = mappedState.screenState,
                shimmerLayout = R.layout.fragment_chat_shimmer,
                cacheStateBinding = topStateBox,
                showLoadingInCacheState = false,
            )
        }

        topicsAdapter.updateIfChanged(state.topics.data ?: emptyList())

        bottomBar.messageEditText.setTextIfChanged(mappedState.messageField)

        actionProgressBar.isVisible = mappedState.isLoading
    }

    override fun ChatFragment.handleEffectByRenderer(
        effect: ChatEffectElm,
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) = when (val mappedEffect = mapper.mapEffect(effect)) {
        is ChatEffectUiElm.OpenReactionChooser -> {
            openReactionChooser(
                messageId = mappedEffect.destMessageId,
                excludeEmojis = mappedEffect.excludeEmojis,
                fragment = this@handleEffectByRenderer,
            )
        }

        is ChatEffectUiElm.OpenMessageActionsChooser -> {
            openActionsChooser(
                messageId = mappedEffect.messageId,
                userId = mappedEffect.userId,
                streamName = mappedEffect.streamName,
                isOwner = mappedEffect.isOwner,
                fragment = this,
            )
        }

        is ChatEffectUiElm.ShowActionErrorWithRetry -> {
            showActionInternetErrorWithRetry(binding.root) {
                store.accept(mappedEffect.retryEvent)
            }
        }

        is ChatEffectUiElm.ShowSnackbarWithText -> {
            showInfo(binding.root, mappedEffect.text.resId)
        }

        ChatEffectUiElm.OpenFileChooser -> openFilePicker()
        ChatEffectUiElm.ShowTopicChangedBecauseNewMessageIsUnreachable -> {
            showShortInfo(binding.root, R.string.new_message_is_unreachable)
        }

        ChatEffectUiElm.ExpandTopicChooser -> binding.bottomBar.topicAutoCompleteTextView.showDropDown()
        is ChatEffectUiElm.OpenMessageEditor -> openMessageEditor(
            messageId = mappedEffect.messageId,
            streamName = mappedEffect.streamName,
            fragment = this,
        )
        is ChatEffectUiElm.OpenMessageTopicChanger -> {
            openMessageTopicChanger(
                messageId = mappedEffect.messageId,
                streamId = mappedEffect.streamId,
                topicName = mappedEffect.topicName,
                fragment = this,
            )
        }
    }

    override fun ChatFragment.onDestroyRendererView() {
        _adapter = null
        _topicsAdapter = null
        _messagesRecyclerLinearLayoutManager = null
    }

    private fun setAdapterToRecyclerView(
        recyclerView: RecyclerView,
        adapter: DiffDelegationAdapter
    ) {
        recyclerView.adapter = adapter
    }

    private fun Fragment.initListeners(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
    ) = with(binding) {
        bottomBar.topicAutoCompleteTextView.observe(noDebouncePredicate = { it.length <= 1 })
            .onEach {
                store.accept(mapper.mapUiEvent(ChatEventUiElm.TopicChanged(it)))
            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        chatTopBar.openAllTopicsButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.OnLeaveTopicClicked))
        }
        bottomBar.messageEditText.observe(noDebouncePredicate = { it.length == 1 }).onEach {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.MessageFieldChanged(it)))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        bottomBar.sendOrAddAttachmentButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.SendMessageAddAttachmentButtonClicked))
        }
        chatTopBar.backButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.GoBackClicked))
        }
        stateBox.setOnRetryClickListener {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.ReloadClicked))
        }

        messagesRecycler.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
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

    private fun openReactionChooser(
        messageId: Long,
        excludeEmojis: List<String>,
        fragment: Fragment,
    ) {
        emojiChooserFactory.create(EMOJI_CHOOSER_REQUEST_KEY, messageId, excludeEmojis)
            .show(fragment.childFragmentManager, EmojiChooserDialogFactory.TAG)
    }

    private fun openActionsChooser(
        messageId: Long,
        userId: Long,
        streamName: String,
        isOwner: Boolean,
        fragment: Fragment,
    ) {
        actionChooserDialogFactory.create(
            resultKey = ACTION_CHOOSER_REQUEST_KEY,
            messageId = messageId,
            senderUserId = userId,
            streamName = streamName,
            isOwner = isOwner,
        ).show(fragment.childFragmentManager, ActionChooserDialogFactory.TAG)
    }

    private fun openMessageEditor(messageId: Long, streamName: String, fragment: Fragment) {
        messageEditorDialogFactory.create(
            messageId = messageId,
            streamName = streamName,
            resultKey = MESSAGE_EDIT_REQUEST_KEY,
        ).show(fragment.childFragmentManager, MessageEditorDialogFactory.TAG)
    }

    private fun openMessageTopicChanger(messageId: Long, streamId: Long, topicName: String, fragment: Fragment) {
        messageTopicChangerDialogFactory.create(
            messageId = messageId,
            streamId = streamId,
            topicName = topicName,
            resultKey = MESSAGE_MOVE_REQUEST_KEY,
        ).show(fragment.childFragmentManager, MessageTopicChangerDialogFactory.TAG)
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
            fragment.viewLifecycleOwner,
        ) { _, bundle ->
            bundle.getParcelableTypeSafe<EmojiChooserResult>(EmojiChooserDialogFactory.BUNDLE_RESULT_KEY)
                ?.let { result ->
                    when (result) {
                        is EmojiChooserResult.EmojiWasChosen -> store.accept(
                            ChatEventUiElm.AddChosenReaction(
                                messageId = result.messageId,
                                reactionName = result.emojiName,
                            ).let { mapper.mapUiEvent(it) }
                        )
                    }
                }
        }
    }

    private fun ChatFragment.initMessageTopicChangerResultListener(
        binding: FragmentChatBinding,
    ) {
        childFragmentManager.setFragmentResultListener(
            MESSAGE_MOVE_REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, bundle ->
            bundle.getParcelableTypeSafe<MessageMoveResult>(ActionChooserDialogFactory.BUNDLE_RESULT_KEY)
                ?.let { result ->
                    when(result) {
                        is MessageMoveResult.Error -> showInfo(binding.root, result.errorMessage)
                        is MessageMoveResult.MovedMessage -> store.accept(mapper.mapUiEvent(ChatEventUiElm.MessageMovedToNewTopic(result.newTopic)))
                    }
                }

        }
    }

    private fun ChatFragment.initMessageEditorResultListener(
        binding: FragmentChatBinding,
    ) {
        childFragmentManager.setFragmentResultListener(
            MESSAGE_EDIT_REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, bundle ->
            bundle.getParcelableTypeSafe<MessageEditorResult>(ActionChooserDialogFactory.BUNDLE_RESULT_KEY)
                ?.let { result ->
                    when(result) {
                        is MessageEditorResult.Error -> showInfo(binding.root, result.errorMessage)
                        is MessageEditorResult.EditedMessage -> Unit
                    }
                }
        }
    }

    private fun ChatFragment.initActionChooserResultListener(
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>,
        binding: FragmentChatBinding,
    ) {
        childFragmentManager.setFragmentResultListener(
            ACTION_CHOOSER_REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, bundle ->
            bundle.getParcelableTypeSafe<ActionChooserResult>(ActionChooserDialogFactory.BUNDLE_RESULT_KEY)
                ?.let { result ->
                    when(result) {
                        is ActionChooserResult.AddReaction -> store.accept(mapper.mapUiEvent(ChatEventUiElm.AddReactionClicked(result.messageId)))
                        is ActionChooserResult.CopiedMessage -> Unit
                        is ActionChooserResult.EditMessage -> store.accept(mapper.mapUiEvent(ChatEventUiElm.EditMessageClicked(result.messageId)))
                        is ActionChooserResult.MoveMessage -> store.accept(mapper.mapUiEvent(ChatEventUiElm.MoveMessageClicked(result.messageId)))
                        is ActionChooserResult.RemovedMessage -> Unit
                        is ActionChooserResult.OpenedProfile -> Unit
                        is ActionChooserResult.Error -> showInfo(binding.root, result.errorMessage)
                    }
                }

        }
    }
}