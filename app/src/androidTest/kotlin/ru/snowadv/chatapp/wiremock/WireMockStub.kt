package ru.snowadv.chatapp.wiremock

import com.github.tomakehurst.wiremock.junit.WireMockRule

internal interface WireMockStub {
    fun applyToWiremock(rule: WireMockRule)
}