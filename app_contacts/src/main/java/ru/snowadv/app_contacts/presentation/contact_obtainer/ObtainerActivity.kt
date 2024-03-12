package ru.snowadv.app_contacts.presentation.contact_obtainer

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.snowadv.app_contacts.R
import ru.snowadv.app_contacts.presentation.model.Contact
import ru.snowadv.app_contacts.presentation.contact_service.ContactService

internal class ObtainerActivity : AppCompatActivity() {

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    companion object {
        const val OBTAINER_RESULT_BUNDLE_KEY = ContactService.OBTAINER_RESULT_BUNDLE_KEY
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_obtainer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        subscribeAndSetData()
    }

    override fun onResume() {
        super.onResume()
        startContactService()
    }

    private fun startContactService() {
        startService(Intent(this, ContactService::class.java))
    }

    private fun subscribeAndSetData() {
        val receiver = ObtainerBroadcastReceiver(
            onContactsReceived = {
                finishWithOkResult(ArrayList(it))
            },
            onError = ::finishWithError
        )
        val filter = IntentFilter().apply {
            addAction(ContactService.CONTACTS_RECEIVED_ACTION)
            addAction(ContactService.CONTACTS_FETCH_FAILED_ACTION)
        }
        localBroadcastManager.registerReceiver(receiver, filter)
    }
    private fun finishWithError() {
        setResult(RESULT_CANCELED) // cancelling (no permission)
        finish()
    }

    private fun finishWithOkResult(contacts: ArrayList<Contact>) {
        Intent().apply {
            putParcelableArrayListExtra(OBTAINER_RESULT_BUNDLE_KEY, contacts)
            setResult(RESULT_OK, this)
        }
        finish()
    }
}