package ru.snowadv.chatapp.mock.config

import ru.snowadv.model.BaseUrlProvider
import javax.inject.Inject

internal class WiremockBaseUrlProviderImpl @Inject constructor(): BaseUrlProvider {
    override val baseUrl: String = "http://127.0.0.1:8080/"
    override val apiUrl: String = "http://127.0.0.1:8080/api/v1/"
}