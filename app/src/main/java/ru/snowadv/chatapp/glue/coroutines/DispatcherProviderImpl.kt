package ru.snowadv.chatapp.glue.coroutines

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.snowadv.model.DispatcherProvider
import javax.inject.Inject

@Reusable
class DispatcherProviderImpl @Inject constructor(): DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}