package ru.snowadv.chatapp.rule

import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
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
        private val stubs by lazy {
            listOf(
                // Chat screen
                ChatTransformerStub(),
                // Channels screen
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/streams.*",
                    assetsPath = "channels/streams.json",
                    method = RequestMethod.GET,
                ),
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/users/me/[0-9]+/topics.*",
                    assetsPath = "channels/topics.json",
                    method = RequestMethod.GET,
                ),
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/users/me/subscriptions.*",
                    assetsPath = "channels/subscriptions.json",
                    method = RequestMethod.GET,
                ),
                // People screen
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/users/?\$",
                    assetsPath = "people/people.json",
                    method = RequestMethod.GET,
                ),
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/realm/presence.*",
                    assetsPath = "people/realm_presence.json",
                    method = RequestMethod.GET,
                ),
                // Profile screen
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/users/[0-9]+.*",
                    assetsPath = "profile/profile.json",
                    method = RequestMethod.GET,
                ),
                AssetStubMatcher(
                    urlRegex = ".*/api/v1/users/[0-9]+/presence.*",
                    assetsPath = "profile/presence.json",
                    method = RequestMethod.GET,
                ),
            )
        }
    }

    val wiremock = WireMockRule(wireMockConfig().extensions(ChatServerResponseTransformer()).notifier(ConsoleNotifier(true)))
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