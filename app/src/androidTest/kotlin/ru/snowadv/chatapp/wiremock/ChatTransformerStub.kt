package ru.snowadv.chatapp.wiremock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.UrlPattern

internal class ChatTransformerStub : WireMockStub {
    override fun applyToWiremock(rule: WireMockRule) {
        val pattern = WireMock.urlPathMatching(".*") // will match all urls
        val matcher = WireMock.any(pattern)
        rule.stubFor(matcher.willReturn(WireMock.ok().withTransformers(
            "chat_server_response_transformer",
        )))
    }
}