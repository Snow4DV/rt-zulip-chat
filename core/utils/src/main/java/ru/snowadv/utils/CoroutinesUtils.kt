package ru.snowadv.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

fun CoroutineScope.hasAnyCoroutinesRunning(): Boolean {
    return coroutineContext[Job]?.children?.any { it.isActive } ?: false
}

fun CoroutineScope.hasMoreThanOneCoroutineRunning(): Boolean {
    return (coroutineContext[Job]?.children?.count { it.isActive } ?: 0) > 1
}