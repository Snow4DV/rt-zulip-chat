package ru.snowadv.voiceapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.snowadv.navigation.activity.NavigatorHolder
import ru.snowadv.voiceapp.R
import ru.snowadv.voiceapp.databinding.ActivityMainBinding

internal class MainActivity : AppCompatActivity(), NavigatorHolder {
    override val navigator: AppNavigator
        get() = AppNavigator(this, R.id.fragment_container)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        getNavigatorHolder().removeNavigator()
        super.onPause()
    }
}