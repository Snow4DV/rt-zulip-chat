package ru.snowadv.chatapp.server.wiremock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.UrlPattern
import ru.snowadv.chatapp.server.wiremock.api.WireMockStub
import ru.snowadv.chatapp.util.AssetsUtils.fromAssets

internal data class AssetStubMatcher(val urlRegex: String, val assetsPath: String, val method: RequestMethod) :
    WireMockStub {
    override fun applyToWiremock(rule: WireMockRule) {
        val pattern = WireMock.urlPathMatching(urlRegex)
        val matcher = matcherForPatternAndMethod(pattern, method)
        rule.stubFor(matcher.willReturn(WireMock.ok().withBody(fromAssets(assetsPath))))
    }

    private fun matcherForPatternAndMethod(pattern: UrlPattern, method: RequestMethod): MappingBuilder {
        return when(method) {
            RequestMethod.POST -> WireMock.post(pattern)
            RequestMethod.GET -> WireMock.get(pattern)
            RequestMethod.PUT -> WireMock.put(pattern)
            RequestMethod.DELETE -> WireMock.delete(pattern)
            RequestMethod.PATCH -> WireMock.patch(pattern)
            RequestMethod.ANY -> WireMock.any(pattern)
            else -> throw IllegalArgumentException("Wrong request method")
        }
    }

}