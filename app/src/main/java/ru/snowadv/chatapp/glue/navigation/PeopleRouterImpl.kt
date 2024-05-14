package ru.snowadv.chatapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class PeopleRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    PeopleRouter {
    override fun openProfile(userId: Long) {
        ciceroneRouter.navigateTo(screens.Profile(userId))
    }
}