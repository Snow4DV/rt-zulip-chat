package ru.snowadv.chat_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.chat_impl.presentation.chat.ChatFragment
import javax.inject.Inject

@Reusable
internal class ChatScreenFactoryImpl @Inject constructor(): ChatScreenFactory {
    override fun create(streamName: String, topicName: String): Fragment = ChatFragment.newInstance(
        streamName = streamName,
        topicName = topicName,
    )
}