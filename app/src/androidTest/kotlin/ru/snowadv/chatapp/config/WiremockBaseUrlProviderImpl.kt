package ru.snowadv.chatapp.config

import ru.snowadv.model.BaseUrlProvider

internal object WiremockBaseUrlProviderImpl: BaseUrlProvider {
    override val baseUrl: String = "http://127.0.0.1:8080/"
    override val apiUrl: String = "http://127.0.0.1:8080/api/v1/"
}