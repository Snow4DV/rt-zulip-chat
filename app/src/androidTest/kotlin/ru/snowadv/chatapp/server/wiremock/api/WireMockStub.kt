package ru.snowadv.chatapp.server.wiremock.api

import com.github.tomakehurst.wiremock.junit.WireMockRule

internal interface WireMockStub {
    fun applyToWiremock(rule: WireMockRule)
}