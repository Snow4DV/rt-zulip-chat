package ru.snowadv.people_api.domain.navigation

interface PeopleRouter {
    fun openProfile(userId: Long)
}