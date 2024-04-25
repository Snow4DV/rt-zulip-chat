package ru.snowadv.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


/**
 * This interceptor makes it possible to increase timeout per-request
 * It is used to respect event_queue_longpoll_timeout_seconds sent by server
 */
class TimeoutSetterInterceptor : Interceptor {

    companion object {
        const val CONNECT_TIMEOUT_HEADER = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT_HEADER = "READ_TIMEOUT"
        const val WRITE_TIMEOUT_HEADER = "WRITE_TIMEOUT"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val connectTimeout =
            request.header(CONNECT_TIMEOUT_HEADER)?.toIntOrNull() ?: chain.connectTimeoutMillis()
        val readTimeout =
            request.header(READ_TIMEOUT_HEADER)?.toIntOrNull() ?: chain.readTimeoutMillis()
        val writeTimeout =
            request.header(WRITE_TIMEOUT_HEADER)?.toIntOrNull() ?: chain.writeTimeoutMillis()

        // Synthetic headers are removed from request
        val newRequest = request.newBuilder()
            .removeHeader(CONNECT_TIMEOUT_HEADER)
            .removeHeader(READ_TIMEOUT_HEADER)
            .removeHeader(WRITE_TIMEOUT_HEADER)
            .build()


        return chain
            .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
            .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
            .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
            .proceed(newRequest)
    }
}