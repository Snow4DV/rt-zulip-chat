package ru.snowadv.chat.presentation.emoji_chooser

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.chat.presentation.emoji_chooser.view_model.EmojiChooserViewModel
import ru.snowadv.chat.presentation.emoji_chooser.view_model.EmojiChooserViewModelFactory
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.fragment.FragmentDataObserver

class EmojiChooserBottomSheetDialog constructor() : BottomSheetDialogFragment(),
    FragmentDataObserver<FragmentEmojiChooserBinding, EmojiChooserViewModel, EmojiChooserBottomSheetDialog> by EmojiChooserDataObserver() {

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
    private val viewModel: EmojiChooserViewModel by viewModels(
        factoryProducer = { EmojiChooserViewModelFactory(ChatGraph.getEmojisUseCase) }
    )
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
        setStateBoxBackgroundColor()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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