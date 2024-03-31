package ru.snowadv.chat.presentation.navigation

interface ChatRouter {
    fun goBack()
    fun openProfile(profileId: Long)
}