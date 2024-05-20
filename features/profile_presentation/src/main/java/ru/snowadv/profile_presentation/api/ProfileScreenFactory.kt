package ru.snowadv.profile_presentation.api

import androidx.fragment.app.Fragment

interface ProfileScreenFactory {
    fun create(profileId: Long?): Fragment
}