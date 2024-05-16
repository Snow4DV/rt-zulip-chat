package ru.snowadv.chatapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch
import ru.snowadv.auth_storage.di.holder.AuthStorageComponentHolder
import ru.snowadv.chatapp.R
import ru.snowadv.chatapp.databinding.ActivityMainBinding
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import ru.snowadv.chatapp.glue.injector.ReleaseModulesInjector
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    private val navigator: AppNavigator = AppNavigator(this, R.id.fragment_container)
    private lateinit var binding: ActivityMainBinding

    @Inject
    internal lateinit var router: Router
    @Inject
    internal lateinit var screens: Screens

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeComponents()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun loadAuthentication() {
        AuthStorageComponentHolder.get().authStorageRepository.let { authStorageRepository ->
            lifecycleScope.launch {
                authStorageRepository.loadAuthFromDatabase()?.let {
                    router.newRootScreen(screens.Home())
                } ?: run {
                    router.newRootScreen(screens.Login())
                }
            }
        }
    }

    private fun initializeComponents() {
        // Authentication will only be loaded during initial components initialization
        // Needed for UI-testing purposes
        if (!AppModuleComponentHolder.isInitialized) {
            ReleaseModulesInjector.inject(this)
            AppModuleComponentHolder.getComponent().inject(this)
            loadAuthentication()
        } else {
            AppModuleComponentHolder.getComponent().inject(this)
        }
    }
}