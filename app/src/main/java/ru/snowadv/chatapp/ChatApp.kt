package ru.snowadv.chatapp

import android.app.Application
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.snowadv.auth_storage.di.holder.AuthStorageComponentHolder
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import ru.snowadv.chatapp.glue.injector.ReleaseModulesInjector
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

class ChatApp: Application()