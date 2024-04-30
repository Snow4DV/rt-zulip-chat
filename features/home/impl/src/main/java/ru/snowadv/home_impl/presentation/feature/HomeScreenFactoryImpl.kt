package ru.snowadv.home_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.home_impl.presentation.home.HomeFragment
import ru.snowadv.home_api.presentation.feature.HomeScreenFactory
import javax.inject.Inject

@Reusable
internal class HomeScreenFactoryImpl @Inject constructor() : HomeScreenFactory {
    override fun create(): Fragment = HomeFragment.newInstance()
}