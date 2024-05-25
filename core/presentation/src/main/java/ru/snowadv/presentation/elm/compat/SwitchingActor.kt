package ru.snowadv.presentation.elm.compat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.switcher.Switcher
import kotlin.reflect.KClass

/**
 * This class is required to be able to cancel running commands. It already exists in the latest
 * version of elmslie but i had to backport it due to vivid not releasing any alphas/RC/releases
 * since 2023. Remove when new version of elmslie is released.
 */
abstract class SwitchingActor<Command : Any, Event : Any>: Actor<Command, Event>() {

    private val switchers = mutableMapOf<KClass<out Any>, Switcher>()
    private val mutex = Mutex()

    /**
     * Executes a command. This method is performed on the [Dispatchers.IO]
     * [kotlinx.coroutines.Dispatchers.IO] which is set by ElmslieConfig.ioDispatchers()
     */

    protected fun <T : Any, Command : Any> Flow<T>.asSwitchFlow(command: Command, delayMillis: Long = 0): Flow<T> {
        return flow {
            val switcher = mutex.withLock {
                switchers.getOrPut(command::class) {
                    Switcher()
                }
            }
            switcher.switch(delayMillis) { this@asSwitchFlow }.collect {
                emit(it)
            }
        }
    }

    protected fun <T : Any> cancelSwitchFlow(command: KClass<out Any>): Flow<T> {
        return switchers[command]?.cancel() ?: emptyFlow()
    }
}