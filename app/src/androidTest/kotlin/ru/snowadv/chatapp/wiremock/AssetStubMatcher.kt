package ru.snowadv.chatapp.wiremock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.UrlPattern

internal data class AssetStubMatcher(val urlRegex: String, val assetsPath: String, val method: RequestMethod) : WireMockStub {
    override fun applyToWiremock(rule: WireMockRule) {
        val pattern = WireMock.urlPathMatching(urlRegex)
        val matcher = matcherForPatternAndMethod(pattern, method)
        rule.stubFor(matcher.willReturn(WireMock.ok()))
    }

    private fun matcherForPatternAndMethod(pattern: UrlPattern, method: RequestMethod): MappingBuilder {
        return when(method) {
            RequestMethod.POST -> WireMock.post(pattern)
            RequestMethod.GET -> WireMock.get(pattern)
            RequestMethod.PUT -> WireMock.put(pattern)
            RequestMethod.DELETE -> WireMock.delete(pattern)
            RequestMethod.PATCH -> WireMock.patch(pattern)
            else -> throw IllegalArgumentException("Wrong request method")
        }
    }

}