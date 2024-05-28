package ru.snowadv.chatapp.server.wiremock

import com.github.tomakehurst.wiremock.http.RequestMethod
import ru.snowadv.chatapp.server.wiremock.api.WireMockStub
import ru.snowadv.chatapp.server.wiremock.api.WireMockStubsProvider
import javax.inject.Inject

internal class StaticStubsProvider @Inject constructor() : WireMockStubsProvider {
    override val stubs: List<WireMockStub> by lazy {
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