package ru.snowadv.chat_presentation.api

import androidx.fragment.app.Fragment

interface ChatScreenFactory {
    fun create(streamId: Long, streamName: String, topicName: String?): Fragment
}