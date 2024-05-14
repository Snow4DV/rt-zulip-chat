package ru.snowadv.profile_api.presentation

import androidx.fragment.app.Fragment

interface ProfileScreenFactory {
    fun create(profileId: Long?): Fragment
}