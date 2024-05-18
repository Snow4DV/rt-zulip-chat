package ru.snowadv.chatapp.rule

import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.di.holder.TestAppModuleComponentHolder
import ru.snowadv.chatapp.server.ChatServerResponseTransformer
import ru.snowadv.chatapp.server.wiremock.AssetStubMatcher
import ru.snowadv.chatapp.server.wiremock.ChatTransformerStub
import ru.snowadv.chatapp.server.wiremock.api.WireMockStubsProvider
import javax.inject.Inject

internal class WiremockTestRule: TestWatcher() {

    @Inject
    lateinit var wiremock: WireMockRule
    @Inject
    lateinit var stubsProvider: WireMockStubsProvider

    override fun apply(base: Statement?, description: Description?): Statement {
        TestAppModuleComponentHolder.getComponent().inject(this)
        stubsProvider.injectToWireMock(wiremock)
        return RuleChain.outerRule(wiremock).apply(base, description)
    }
}