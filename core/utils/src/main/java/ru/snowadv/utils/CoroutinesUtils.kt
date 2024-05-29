package ru.snowadv.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T1, T2, R> asyncAwait(
    s1: suspend CoroutineScope.() -> T1,
    s2: suspend CoroutineScope.() -> T2,
    transform: suspend (T1, T2) -> R,
): R {
    return coroutineScope {
        val result1 = async(block = s1)
        val result2 = async(block = s2)

        transform(
            result1.await(),
            result2.await()
        )
    }
}

public inline fun <T, R> T.runSuspendCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}