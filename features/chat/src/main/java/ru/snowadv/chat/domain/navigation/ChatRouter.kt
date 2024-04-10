package ru.snowadv.chat.domain.navigation

interface ChatRouter {
    fun goBack()
    fun openProfile(profileId: Long)
}