package ru.snowadv.chatapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.snowadv.chatapp.R
import ru.snowadv.chatapp.databinding.ActivityMainBinding
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    private val navigator: AppNavigator = AppNavigator(this, R.id.fragment_container)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppModuleComponentHolder.getComponent().inject(this)
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
}