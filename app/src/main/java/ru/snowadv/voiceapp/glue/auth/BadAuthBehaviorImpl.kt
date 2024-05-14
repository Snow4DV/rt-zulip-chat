package ru.snowadv.voiceapp.glue.auth

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.network.api.BadAuthBehavior
import javax.inject.Inject

@Reusable
class BadAuthBehaviorImpl @Inject constructor(private val authDataRepository: AuthDataRepository, private val router: Router): BadAuthBehavior {
    override fun onBadAuth() {
        // TODO: Implement logic when session dies:
        // 1) Remove user from auth repository
        // 2) Reroute to login screen (with backstack clear)
    }
}