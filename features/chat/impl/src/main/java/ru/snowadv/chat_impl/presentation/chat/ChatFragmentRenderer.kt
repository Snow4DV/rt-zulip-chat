package ru.snowadv.chat_impl.presentation.chat

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.noties.markwon.Markwon
import ru.snowadv.chat_impl.R
import ru.snowadv.chat_impl.databinding.FragmentChatBinding
import ru.snowadv.chat_impl.di.ChatFeatureComponentHolder
import ru.snowadv.chat_impl.domain.util.PaginationConfig
import ru.snowadv.chat_impl.presentation.adapter.DateSplitterAdapterDelegate
import ru.snowadv.chat_impl.presentation.adapter.IncomingMessageAdapterDelegate
import ru.snowadv.chat_impl.presentation.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat_impl.presentation.adapter.PaginationStatusAdapterDelegate
import ru.snowadv.chat_impl.presentation.chat.elm.ChatEffectElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatStateElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.EmojiChooserBottomSheetDialog
import ru.snowadv.chat_impl.presentation.model.ChatAction
import ru.snowadv.chat_impl.presentation.model.ChatPaginationStatus
import ru.snowadv.chat_impl.presentation.util.AdapterUtils.submitListAndKeepScrolledToBottom
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
import javax.inject.Inject
import kotlin.math.abs

internal class ChatFragmentRenderer :
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized" }

    private var _messagesRecyclerLinearLayoutManager: LinearLayoutManager? = null
    private val messagesRecyclerLinearLayoutManager: LinearLayoutManager get() =
        requireNotNull(_messagesRecyclerLinearLayoutManager) { "Linear Layout Manager wasn't initialized for messages recycler" }

    @Inject
    internal lateinit var splitterDateFormatter: DateFormatter
    @Inject
    internal lateinit var dateTimeFormatter: DateTimeFormatter
    @Inject
    internal lateinit var markwon: Markwon

    companion object {
        const val EMOJI_CHOOSER_REQUEST_KEY = "emoji_chooser_request"
    }

    init {
        ChatFeatureComponentHolder.getComponent().inject(this)
    }

    override fun ChatFragment.onRendererViewCreated(
        binding: FragmentChatBinding,
        store: Store<ChatEventElm, ChatEffectElm, ChatStateElm>
    ) {
        initEmojiChooserResultListener(this, store)
        _adapter = initDelegateAdapter(store).also {
            setAdapterToRecyclerView(
                requireContext(),
                binding.messagesRecycler,
                it
            )
        }
        _messagesRecyclerLinearLayoutManager = binding.messagesRecycler.layoutManager as? LinearLayoutManager
            ?: error("Wrong LinearLayoutManager is set to messages recycler")
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

        adapter.submitListAndKeepScrolledToBottom(
            recycler = binding.messagesRecycler,
            list = listOf(state.paginationStatus) + (state.screenState.getCurrentData() ?: emptyList()),
        ) {
            stateBox.inflateState(state.screenState, R.layout.fragment_chat_shimmer, topStateBox)
        }

        binding.bottomBar.messageEditText.setTextIfEmpty(state.messageField)

        actionProgressBar.isVisible = state.changingReaction || state.sendingMessage || state.uploadingFile
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

            ChatEffectElm.OpenFileChooser -> openFilePicker()
        }
    }

    override fun ChatFragment.onDestroyRendererView() {
        _adapter = null
        _messagesRecyclerLinearLayoutManager = null
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
            if (messagesRecyclerLinearLayoutManager.findFirstVisibleItemPosition() < PaginationConfig.TOP_MESSAGES_TO_FETCH_COUNT
                    && scrollY - oldScrollY < 0) {
                store.accept(ChatEventElm.Ui.ScrolledToNTopMessages)
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