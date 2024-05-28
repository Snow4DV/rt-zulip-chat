package ru.snowadv.chat_presentation.navigation

interface ChatRouter {
    fun goBack()
    fun openProfile(profileId: Long)
}