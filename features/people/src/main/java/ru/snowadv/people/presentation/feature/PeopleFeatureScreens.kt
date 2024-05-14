package ru.snowadv.people.presentation.feature

import androidx.fragment.app.Fragment
import ru.snowadv.people.presentation.people_list.PeopleFragment

object PeopleFeatureScreens {
    fun People(): Fragment {
        return PeopleFragment.newInstance()
    }
}