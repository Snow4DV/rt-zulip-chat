package ru.snowadv.app_contacts.presentation.contact_list

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.app_contacts.presentation.contact_obtainer.ObtainerActivity
import ru.snowadv.app_contacts.R
import ru.snowadv.app_contacts.databinding.ActivityContactsBinding
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.presentation.contact_list.adapter.ContactsAdapter
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsActivityEvent
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsUiEvent
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsViewModel

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding

    private val adapter = ContactsAdapter(emptyList())

    private val viewModel by viewModels<ContactsViewModel>()

    private val requestContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.event(ContactsUiEvent.FetchContactsPermissionAllowed)
        } else {
            viewModel.event(ContactsUiEvent.FetchContactsPermissionDenied)
        }
    }

    private val startObtainerActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.extras?.let {
                        viewModel.event(
                            ContactsUiEvent.FetchedNewContacts(
                                contacts = getContactsFromBundle(
                                    bundle = it,
                                    key = "result"
                                )
                            )
                        )
                    }
                }

                Activity.RESULT_CANCELED -> {
                    viewModel.event(ContactsUiEvent.ObtainCancelledOrFailed)
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
        subscribeAndSetData()
    }

    private fun initListeners() {
        binding.getContactsButton.setOnClickListener {
            viewModel.event(ContactsUiEvent.FetchContactsButtonClicked)
        }
    }

    private fun subscribeAndSetData() {
        binding.contactsList.adapter = adapter
        viewModel.activityEventFlow.onEach {
            when(it) {
                is ContactsActivityEvent.OpenObtainerActivity -> {
                    launchObtainerActivity()
                }
                is ContactsActivityEvent.AskContactsPermission -> {
                    obtainPermissionAndProceed(
                        requestPermissionLauncher = requestContactsPermissionLauncher,
                        permission = Manifest.permission.READ_CONTACTS,
                        rationale = getString(R.string.need_contacts_to_show_list)
                    ) {
                        viewModel.event(ContactsUiEvent.FetchContactsPermissionAllowed)
                    }
                }
                is ContactsActivityEvent.ShowNoPermissionToast -> {
                    showUnableToProceedWithoutPermissionToast()
                }
                is ContactsActivityEvent.ShowObtainCancelledToast -> {
                    showContactsFetchFailedToast()
                }
            }
        }.launchIn(lifecycleScope)
        viewModel.state.onEach {
            adapter.setContacts(it.contacts)
        }.launchIn(lifecycleScope)
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

    private fun showContactsFetchFailedToast() {
        Toast.makeText(this, getString(R.string.error_while_fetching_contacts), Toast.LENGTH_LONG)
            .show()
    }

    private fun launchObtainerActivity() {
        startObtainerActivityForResult.launch(Intent(this, ObtainerActivity::class.java))
    }
}