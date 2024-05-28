package ru.snowadv.test_utils.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

object TestUtils {
    fun <T> Flow<T>.runBlockingToList(): List<T> {
        return runBlocking {
            buildList {
                collect {
                    add(it)
                }
            }
        }
    }

    fun <T> Flow<T>.runBlocking() {
        return runBlocking {
            collect {}
        }
    }
}