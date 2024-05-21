package ru.snowadv.voiceapp

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_impl.di.AuthDataModuleComponentHolder
import ru.snowadv.voiceapp.di.dagger.AppComponent
import ru.snowadv.voiceapp.di.holder.AppModuleComponentHolder
import ru.snowadv.voiceapp.glue.injector.ModulesInjector
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

internal class ChatApp: Application() {
    private val authScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Inject
    internal lateinit var router: Router
    @Inject
    internal lateinit var screens: Screens

    private val authDataRepo by lazy(mode = LazyThreadSafetyMode.NONE) { AuthDataModuleComponentHolder.get().authDataRepo }

    override fun onCreate() {
        ModulesInjector.inject(this)
        AppModuleComponentHolder.getComponent().inject(this)
        super.onCreate()
        authScope.launch {
            authDataRepo.loadAuthFromDatabase()?.let {
                router.newRootScreen(screens.Home())
            } ?: run {
                router.newRootScreen(screens.Login())
            }
        }
    }

    override fun onTerminate() {
        authScope.cancel()
        super.onTerminate()
    }
}