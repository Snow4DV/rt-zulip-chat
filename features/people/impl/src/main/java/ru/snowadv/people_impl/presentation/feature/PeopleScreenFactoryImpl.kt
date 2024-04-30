package ru.snowadv.people_impl.presentation.feature

import androidx.fragment.app.Fragment
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.people_impl.presentation.people_list.PeopleFragment
import javax.inject.Inject

internal class PeopleScreenFactoryImpl @Inject constructor() : PeopleScreenFactory {
    override fun create(): Fragment = PeopleFragment.newInstance()
}