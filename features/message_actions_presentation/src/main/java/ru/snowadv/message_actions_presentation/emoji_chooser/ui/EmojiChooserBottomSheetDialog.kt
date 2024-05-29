package ru.snowadv.message_actions_presentation.emoji_chooser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import ru.snowadv.message_actions_presentation.api.model.EmojiChooserResult
import ru.snowadv.message_actions_presentation.api.screen_factory.EmojiChooserDialogFactory.Companion.BUNDLE_RESULT_KEY
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.message_actions_presentation.databinding.FragmentEmojiChooserBinding
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class EmojiChooserBottomSheetDialog : BaseBottomSheetDialogFragment<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>(),
    ElmFragmentRenderer<EmojiChooserBottomSheetDialog, FragmentEmojiChooserBinding, EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    by EmojiChooserRenderer() {

    companion object {
        const val DEFAULT_ARG_RESULT_KEY = "emoji_chooser_result"
        const val DEFAULT_ARG_MESSAGE_ID = -1L

        const val ARG_RESULT_KEY = "arg_result_key"
        const val ARG_MESSAGE_ID_KEY = "message_id"
        const val ARG_EXCLUDE_EMOJIS_KEY = "exclude_emojis_key"
        fun newInstance(resultKey: String, messageId: Long, excludeEmojis: Array<String>) =
            EmojiChooserBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    ARG_MESSAGE_ID_KEY to messageId,
                    ARG_EXCLUDE_EMOJIS_KEY to excludeEmojis,
                )
            }
    }

    @Inject
    internal lateinit var emojiChooserStoreFactory: EmojiChooserStoreFactoryElm

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
    private val excludeEmojis: Set<String> by lazy {
        requireNotNull(requireArguments().getStringArray(ARG_EXCLUDE_EMOJIS_KEY)?.toSet()) { "Exclude emojis weren't provided" }
    }

    private var _binding: FragmentEmojiChooserBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override val store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        emojiChooserStoreFactory.create(excludeEmojis.toSet())
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
        _binding = FragmentEmojiChooserBinding.inflate(inflater, container, false)
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

    override fun render(state: EmojiChooserStateElm) {
        renderStateByRenderer(state = state, binding = binding)
    }

    override fun handleEffect(effect: EmojiChooserEffectElm) {
        handleEffectByRenderer(effect = effect, binding = binding, store = store)
    }

    fun finishWithEmoji(emoji: ChatEmoji) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_RESULT_KEY to EmojiChooserResult.EmojiWasChosen(emoji.name, messageId),
            )
        )
        this.dismissAllowingStateLoss()
    }
}