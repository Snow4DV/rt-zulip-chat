package ru.snowadv.chat.presentation.feature

import androidx.fragment.app.Fragment
import ru.snowadv.chat.presentation.chat.ChatFragment

object ChatFeatureScreens {
    fun Chat(streamName: String, topicName: String): Fragment {
        return ChatFragment.newInstance(streamName, topicName)
    }
}