package ru.snowadv.app_contacts.presentation.contact_list

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableArrayListExtra
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.snowadv.app_contacts.presentation.contact_obtainer.ObtainerActivity
import ru.snowadv.app_contacts.R
import ru.snowadv.app_contacts.databinding.ActivityContactsBinding
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.presentation.contact_list.adapter.ContactAdapter

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding

    private val adapter = ContactAdapter(emptyList())

    private val requestContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchObtainerActivity()
        } else {
            showUnableToProceedWithoutPermissionToast()
        }
    }

    private val startObtainerActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.extras?.let {
                        adapter.setContacts(
                            getContactsFromBundle(
                                it,
                                "result"
                            )
                        )
                    }
                }

                Activity.RESULT_CANCELED -> {
                    showContactFetchFailedToast()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initListeners()
        subscribeAndSetData(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState) // save view states
        outState.putParcelableArrayList("contacts", ArrayList(adapter.contacts))
    }

    private fun initListeners() {
        binding.getContactsButton.setOnClickListener {
            obtainPermissionAndProceed(
                requestPermissionLauncher = requestContactsPermissionLauncher,
                permission = Manifest.permission.READ_CONTACTS,
                rationale = getString(R.string.need_contacts_to_show_list)
            ) {
                launchObtainerActivity()
            }
        }
    }

    private fun subscribeAndSetData(savedInstanceState: Bundle?) {
        binding.contactsList.adapter = adapter
        savedInstanceState?.let {
            adapter.setContacts(getContactsFromBundle(it, "contacts"))
        }
    }

    private fun getContactsFromBundle(bundle: Bundle, key: String): List<Contact> {
        bundle.apply {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelableArrayList(key, Contact::class.java) ?: emptyList()
            } else {
                @Suppress("DEPRECATION")
                getParcelableArrayList<Contact>(key)?.filterIsInstance<Contact>() ?: emptyList()
            }
        }
    }

    private inline fun obtainPermissionAndProceed(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        permission: String,
        rationale: String,
        onSuccess: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                onSuccess()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, permission
            ) -> {
                showRationaleDialog(requestPermissionLauncher, rationale, permission)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun showRationaleDialog(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        rationale: String,
        permission: String
    ) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(rationale)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                requestPermissionLauncher.launch(permission)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                showUnableToProceedWithoutPermissionToast()
            }
            .create()
            .show()
    }

    private fun showUnableToProceedWithoutPermissionToast() {
        Toast.makeText(
            this,
            getString(R.string.cant_proceed_without_permission), Toast.LENGTH_LONG
        ).show()
    }

    private fun showContactFetchFailedToast() {
        Toast.makeText(this, getString(R.string.error_while_fetching_contacts), Toast.LENGTH_LONG)
            .show()
    }

    private fun launchObtainerActivity() {
        startObtainerActivityForResult.launch(Intent(this, ObtainerActivity::class.java))
    }
}