package ru.snowadv.chat_presentation.chat.ui.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.chat_presentation.api.ChatScreenFactory
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import javax.inject.Inject

@Reusable
internal class ChatScreenFactoryImpl @Inject constructor(): ChatScreenFactory {
    override fun create(streamName: String, topicName: String): Fragment = ChatFragment.newInstance(
        streamName = streamName,
        topicName = topicName,
    )
}