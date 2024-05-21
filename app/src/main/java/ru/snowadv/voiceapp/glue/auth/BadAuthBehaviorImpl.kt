package ru.snowadv.voiceapp.glue.auth

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class BadAuthBehaviorImpl @Inject constructor(private val router: Router, private val screens: Screens): BadAuthBehavior {
    override fun onBadAuth() {
        router.newRootScreen(screens.Login()) // screen will clear auth once it is opened
    }
}