package ru.snowadv.app_contacts.presentation.contact_obtainer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.snowadv.app_contacts.presentation.contact_service.ContactService
import ru.snowadv.app_contacts.presentation.contact_service.ContactService.Companion.OBTAINER_RESULT_BUNDLE_KEY
import ru.snowadv.app_contacts.presentation.model.Contact

internal class ObtainerBroadcastReceiver(
    private val onContactsReceived: (result: List<Contact>) -> Unit,
    private val onError: () -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            ContactService.CONTACTS_RECEIVED_ACTION -> {
                intent.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getParcelableArrayListExtra(OBTAINER_RESULT_BUNDLE_KEY, Contact::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        getParcelableArrayListExtra<Contact>(OBTAINER_RESULT_BUNDLE_KEY)?.filterIsInstance<Contact>()
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