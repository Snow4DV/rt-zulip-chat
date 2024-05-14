package ru.snowadv.chatapp.rule

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.server.ChatServerResponseTransformer
import ru.snowadv.chatapp.wiremock.AssetStubMatcher
import ru.snowadv.chatapp.wiremock.ChatTransformerStub

internal class WiremockTestRule : TestRule {
    companion object Stubs {
        private val stubs = listOf(
            ChatTransformerStub(),
            AssetStubMatcher(".*/api/v1/messages.*", "chat/messages.json", RequestMethod.GET),
            AssetStubMatcher(".*/api/v1/streams.*", "channels/streams.json", RequestMethod.GET),
            AssetStubMatcher(".*/api/v1/subscriptions.*", "channels/streams.json", RequestMethod.GET),
            AssetStubMatcher(".*/api/v1/users/me/[0-9]+/topics.*", "chat/messages.topics", RequestMethod.GET),
        )
    }

    val wiremock = WireMockRule(wireMockConfig().extensions(ChatServerResponseTransformer()))
    override fun apply(base: Statement?, description: Description?): Statement {
        initStubs()
        return RuleChain.outerRule(wiremock).apply(base, description)
    }

    private fun initStubs() {
        stubs.forEach {
            it.applyToWiremock(wiremock)
        }
    }



}