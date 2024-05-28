package ru.snowadv.chat_presentation.emoji_chooser.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class EmojiChooserBottomSheetDialog : BaseBottomSheetDialogFragment<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>(),
    ElmFragmentRenderer<EmojiChooserBottomSheetDialog, FragmentEmojiChooserBinding, EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    by EmojiChooserRenderer() {

    companion object {
        const val TAG = "emoji_chooser_dialog"

        const val DEFAULT_ARG_RESULT_KEY = "emoji_chooser_result"
        const val DEFAULT_ARG_MESSAGE_ID = -1L

        const val ARG_RESULT_KEY = "arg_result_key"
        const val ARG_MESSAGE_ID_KEY = "message_id"

        const val BUNDLE_CHOSEN_REACTION_NAME = "chosen_emoji_name"
        const val BUNDLE_MESSAGE_ID_KEY = "message_id"
        fun newInstance(resultKey: String, messageId: Long) =
            EmojiChooserBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    ARG_MESSAGE_ID_KEY to messageId,
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

    private var _binding: FragmentEmojiChooserBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override val store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        emojiChooserStoreFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ChatPresentationComponentHolder.getComponent().inject(this)
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
        setStateBoxBackgroundColor()
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

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    fun finishWithEmoji(emoji: ChatEmoji) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_CHOSEN_REACTION_NAME to emoji.name,
                BUNDLE_MESSAGE_ID_KEY to messageId
            )
        )
        this.dismissAllowingStateLoss()
    }

    private fun setStateBoxBackgroundColor() {
        binding.stateBox.root.background =
            ColorDrawable(requireContext().getColor(R.color.on_surface_box_color))
    }
}