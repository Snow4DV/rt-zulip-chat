package ru.snowadv.home_presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.home_presentation.home.HomeFragment
import javax.inject.Inject

@Reusable
internal class HomeScreenFactoryImpl @Inject constructor() :
    ru.snowadv.home_presentation.api.HomeScreenFactory {
    override fun create(): Fragment = HomeFragment.newInstance()
}