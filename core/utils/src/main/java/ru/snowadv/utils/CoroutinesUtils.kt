package ru.snowadv.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

fun CoroutineScope.hasAnyCoroutinesRunning(): Boolean {
    return coroutineContext[Job]?.children?.any { it.isActive } ?: false
}

fun CoroutineScope.hasMoreThanOneCoroutineRunning(): Boolean {
    return (coroutineContext[Job]?.children?.count { it.isActive } ?: 0) > 1
}

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