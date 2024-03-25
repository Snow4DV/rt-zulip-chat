package ru.snowadv.chat.presentation.chat

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.domain.model.emojiMap

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChatBinding.inflate(layoutInflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeIncomingMessage()
        initializeOutgoingMessage()
        initializeButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeIncomingMessage() {
        binding.incomingChatMessage.onReactionClickListener = { count, emojiCode, userReacted ->
            binding.incomingChatMessage.addUpdateReaction(emojiCode, count + 1, (count + 1) % 2 == 0)
        }
        binding.incomingChatMessage.onAddReactionClickListener = {
            binding.incomingChatMessage.addUpdateReaction(emojiMap.values.random().code, 1, false)
        }

    }

    private fun initializeOutgoingMessage() {
        binding.outgoingChatMessage.onReactionClickListener = { count, emojiCode, userReacted ->
            binding.outgoingChatMessage.addUpdateReaction(emojiCode, count + 1, (count + 1) % 2 == 0)
        }
    }

    private fun initializeButtons() {
        with(binding) {
            setAvatarButton.setOnClickListener {
                incomingChatMessage.avatar = getNewAvatar()
            }
            setMessageTextButton.setOnClickListener {
                incomingChatMessage.messageText = getString(R.string.long_message_text_sample)
                outgoingChatMessage.messageText = getString(R.string.long_message_text_sample)
            }
            setName.setOnClickListener {
                incomingChatMessage.usernameText = getString(R.string.username_sample_russian)
            }
            addRandomReactionButton.setOnClickListener {
                incomingChatMessage.addUpdateReaction(emojiMap.values.random().code, 1, false)
                outgoingChatMessage.addUpdateReaction(emojiMap.values.random().code, 1, false)
            }
        }
    }


    private fun getNewAvatar(): Drawable {
        return ResourcesCompat.getDrawable(
            resources,
            R.drawable.test_avatar,
            context?.theme
        ) ?: error("Missing new avatar resource")
    }
}