package ru.snowadv.network.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import ru.snowadv.auth_storage.provider.AuthProvider
import javax.inject.Inject


internal class HeaderBasicAuthInterceptor @Inject constructor(private val authProvider: AuthProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .also { newRequest ->
                authProvider.getAuthorizedUserOrNull()
                    ?.let {
                        newRequest.header("Authorization", Credentials.basic(it.email, it.apiKey))
                    }
            }
            .build()
        return chain.proceed(authenticatedRequest)
    }
}