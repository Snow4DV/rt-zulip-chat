package ru.snowadv.home.presentation.navigation

interface HomeRouter {
    fun goBack()
    fun openTopic(streamId: Long, topicName: String)
    fun openProfile(userId: Long)
}