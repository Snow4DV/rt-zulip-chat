package ru.snowadv.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.network.utils.NetworkUtils.isHttpExceptionWithCodes
import java.io.IOException
import javax.inject.Inject

/**
 * This interceptor's purpose is to catch bad authentication and fire bad auth behavior (injected
 * as dependency).
 */
internal class BadAuthResponseInterceptor @Inject constructor(private val badAuthBehavior: BadAuthBehavior) : Interceptor {
    companion object {
        private val badAuthCodes = setOf(
            "INVALID_API_KEY",
            "UNAUTHORIZED",
            "USER_DEACTIVATED",
            "REALM_DEACTIVATED",
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (exception: IOException) {
            if (exception.isHttpExceptionWithCodes(badAuthCodes)) {
                badAuthBehavior.onBadAuth()
            }
            throw exception
        }
    }
}