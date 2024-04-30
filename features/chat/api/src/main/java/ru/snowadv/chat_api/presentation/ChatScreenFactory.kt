package ru.snowadv.chat_api.presentation

import androidx.fragment.app.Fragment

interface ChatScreenFactory {
    fun create(streamName: String, topicName: String): Fragment
}