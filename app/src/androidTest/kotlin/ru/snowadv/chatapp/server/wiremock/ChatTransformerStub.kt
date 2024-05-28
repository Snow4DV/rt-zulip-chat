package ru.snowadv.chatapp.server.wiremock

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import ru.snowadv.chatapp.server.ChatServerResponseTransformer
import ru.snowadv.chatapp.server.wiremock.api.WireMockStub

internal class ChatTransformerStub : WireMockStub {
    override fun applyToWiremock(rule: WireMockRule) {
        val pattern = WireMock.urlPathMatching(ChatServerResponseTransformer.totalRegex.toString())
        val matcher = WireMock.any(pattern)
        rule.stubFor(matcher.willReturn(WireMock.ok().withTransformers(ChatServerResponseTransformer.NAME)))
    }
}