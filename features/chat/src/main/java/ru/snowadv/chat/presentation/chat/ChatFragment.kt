package ru.snowadv.chat.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModelFactory
import ru.snowadv.presentation.R
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.setColorAndText
import ru.snowadv.presentation.fragment.setStatusBarColor
import ru.snowadv.presentation.fragment.setTopBarColor

class ChatFragment : Fragment(),
    FragmentDataObserver<FragmentChatBinding, ChatViewModel, ChatFragment> by ChatFragmentDataObserver(){

    companion object {
        const val ARG_STREAM_ID_KEY = "stream_id"
        const val ARG_TOPIC_NAME_KEY = "topic_name"
        const val DEFAULT_ARG_STREAM_ID = -1L
        fun newInstance(streamId: Long, topicName: String): Fragment = ChatFragment().apply {
            arguments = bundleOf(
                ARG_STREAM_ID_KEY to streamId,
                ARG_TOPIC_NAME_KEY to topicName,
            )
        }
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val streamId: Long by lazy {
        requireArguments().getLong(ARG_STREAM_ID_KEY, DEFAULT_ARG_STREAM_ID)
            .also {
                if (it == DEFAULT_ARG_STREAM_ID) error("Missing stream id argument")
            }
    }
    private val topicName: String by lazy {
        requireArguments().getString(ARG_TOPIC_NAME_KEY) ?: error("Missing topic name argument")
    }
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(
            router = ChatGraph.router,
            streamId = streamId,
            topicName = topicName,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChatBinding.inflate(layoutInflater).also {
            _binding = it
            addDecoratorToRecycler(it)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
        setStatusBarColor(R.color.primary)
        binding.topBackButtonBar.setTopBarColor(R.color.primary)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewDestroyedToObserver()
        _binding = null
    }

    private fun addDecoratorToRecycler(binding: FragmentChatBinding) {
        binding.messagesRecycler.addItemDecoration(initDecorator())
    }

    private fun initDecorator(): RecyclerView.ItemDecoration {
        return PaddingItemDecorator(
            requireContext(),
            R.dimen.small_common_padding,
            R.dimen.small_common_padding
        )
    }
}