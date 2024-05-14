package ru.snowadv.chatapp.glue.configuration

import dagger.Reusable
import ru.snowadv.model.BaseUrlProvider
import javax.inject.Inject

@Reusable
internal class BaseUrlProviderImpl @Inject constructor(): BaseUrlProvider {
    override val apiUrl: String = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    override val baseUrl: String = "https://tinkoff-android-spring-2024.zulipchat.com/"
}