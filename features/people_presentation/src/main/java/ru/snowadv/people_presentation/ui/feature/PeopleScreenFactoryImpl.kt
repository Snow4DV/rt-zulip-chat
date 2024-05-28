package ru.snowadv.people_presentation.ui.feature

import androidx.fragment.app.Fragment
import ru.snowadv.people_presentation.api.PeopleScreenFactory
import ru.snowadv.people_presentation.ui.PeopleFragment
import javax.inject.Inject

internal class PeopleScreenFactoryImpl @Inject constructor() : PeopleScreenFactory {
    override fun create(): Fragment = PeopleFragment.newInstance()
}