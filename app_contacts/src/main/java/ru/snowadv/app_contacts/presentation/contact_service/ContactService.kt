package ru.snowadv.app_contacts.presentation.contact_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.snowadv.app_contacts.data.ContactRepositoryImpl
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.domain.model.RequestResult
import ru.snowadv.app_contacts.domain.repository.ContactRepository
import ru.snowadv.contacts_provider.ContactDataSource
import ru.snowadv.contacts_provider.impl.CoroutineContactDataSourceImpl

class ContactService: Service() {

    private val contactDataSource: ContactDataSource by lazy { CoroutineContactDataSourceImpl(this.applicationContext) }

    private val supervisorJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + supervisorJob)

    private val repository by lazy { createContactRepository() }

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    companion object {
        const val CONTACTS_RECEIVED_ACTION = "ru.snowadv.app_contacts.presentation.contact_service.CONTACTS_RECEIVED"
        const val CONTACTS_FETCH_FAILED_ACTION= "ru.snowadv.app_contacts.presentation.contact_service.CONTACTS_FETCH_FAILED"
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        supervisorJob.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            processResultAndFinish(repository.getContacts())
        }
        return START_NOT_STICKY
    }

    private fun processResultAndFinish(result: RequestResult<List<Contact>>) {
        when(result) {
            is RequestResult.Success -> {
                Intent(CONTACTS_RECEIVED_ACTION).apply {
                    putParcelableArrayListExtra("result", ArrayList(result.data))
                    localBroadcastManager.sendBroadcast(this)
                }
            }
            else -> {
                localBroadcastManager.sendBroadcast(Intent(CONTACTS_FETCH_FAILED_ACTION))
            }
        }
    }

    private fun createContactDataSource(): ContactDataSource {
        return CoroutineContactDataSourceImpl(this.applicationContext)
    }

    private fun createContactRepository(): ContactRepository {
        return ContactRepositoryImpl(createContactDataSource())
    }
}