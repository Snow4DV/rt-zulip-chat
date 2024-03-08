package ru.snowadv.app_contacts.presentation.contact_obtainer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.presentation.contact_service.ContactService

class ObtainerBroadcastReceiver(
    private val onContactsReceived: (result: List<Contact>) -> Unit,
    private val onError: () -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            ContactService.CONTACTS_RECEIVED_ACTION -> {
                intent.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getParcelableArrayListExtra("result", Contact::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        getParcelableArrayListExtra<Contact>("result")?.filterIsInstance<Contact>()
                    }?.let {
                        onContactsReceived(it)
                    }
                }
            }
            ContactService.CONTACTS_FETCH_FAILED_ACTION -> {
                onError()
            }
        }
    }
}