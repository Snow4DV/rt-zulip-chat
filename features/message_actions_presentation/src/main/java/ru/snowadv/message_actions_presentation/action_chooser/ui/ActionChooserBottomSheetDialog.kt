package ru.snowadv.message_actions_presentation.action_chooser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEffectElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEventElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserStateElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserStoreFactoryElm
import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory.Companion.BUNDLE_RESULT_KEY
import ru.snowadv.message_actions_presentation.databinding.FragmentActionChooserBinding
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.message_actions_presentation.databinding.FragmentEmojiChooserBinding
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.EmojiChooserRenderer
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.PopupHandlingFragment
import ru.snowadv.presentation.fragment.impl.SnackbarPopupHandlingFragment
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ActionChooserBottomSheetDialog : BaseBottomSheetDialogFragment<ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm>(),
    ElmFragmentRenderer<ActionChooserBottomSheetDialog, FragmentActionChooserBinding, ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm>
    by ActionChooserRenderer(), PopupHandlingFragment by SnackbarPopupHandlingFragment() {

    companion object {
        const val DEFAULT_ARG_RESULT_KEY = "action_chooser_result"
        const val DEFAULT_ARG_MESSAGE_ID = -1L
        const val DEFAULT_ARG_USER_ID = -1L

        const val ARG_RESULT_KEY = "arg_result_key"
        const val ARG_MESSAGE_ID_KEY = "message_id"
        const val ARG_USER_ID_KEY = "message_id"
        fun newInstance(resultKey: String, messageId: Long, userId: Long) =
            ActionChooserBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    ARG_MESSAGE_ID_KEY to messageId,
                    ARG_USER_ID_KEY to userId,
                )
            }
    }

    @Inject
    internal lateinit var actionChooserStoreFactoryElm: ActionChooserStoreFactoryElm

    private val resultKey: String by lazy {
        requireArguments().getString(
            ARG_RESULT_KEY,
            DEFAULT_ARG_RESULT_KEY
        )
    }
    private val messageId: Long by lazy {
        requireArguments().getLong(ARG_MESSAGE_ID_KEY, DEFAULT_ARG_MESSAGE_ID)
            .also {
                if (it == DEFAULT_ARG_MESSAGE_ID) error("Missing message id argument")
            }
    }
    private val senderUserId: Long by lazy {
        requireArguments().getLong(ARG_USER_ID_KEY, DEFAULT_ARG_USER_ID)
            .also {
                if (it == DEFAULT_ARG_USER_ID) error("Missing user id argument")
            }
    }

    private var _binding: FragmentActionChooserBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override val store: Store<ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        actionChooserStoreFactoryElm.create(messageId = messageId, senderId = senderUserId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MessageActionsPresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyRendererView()
        _binding = null
    }

    override fun render(state: ActionChooserStateElm) {
        renderStateByRenderer(state = state, binding = binding)
    }

    override fun handleEffect(effect: ActionChooserEffectElm) {
        handleEffectByRenderer(effect = effect, binding = binding, store = store)
    }

    fun finishWithResult(result: ActionChooserResult) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_RESULT_KEY to result,
            )
        )
        this.dismissAllowingStateLoss()
    }
}