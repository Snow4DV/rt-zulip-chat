package ru.snowadv.chatapp.server.wiremock.api

import com.github.tomakehurst.wiremock.junit.WireMockRule

internal interface WireMockStubsProvider {
    val stubs: List<WireMockStub>

    fun injectToWireMock(wireMockRule: WireMockRule) {
        stubs.forEach { it.applyToWiremock(wireMockRule) }
    }
}