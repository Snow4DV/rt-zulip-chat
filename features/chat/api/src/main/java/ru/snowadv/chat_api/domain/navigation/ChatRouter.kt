package ru.snowadv.chat_api.domain.navigation

interface ChatRouter {
    fun goBack()
    fun openProfile(profileId: Long)
}