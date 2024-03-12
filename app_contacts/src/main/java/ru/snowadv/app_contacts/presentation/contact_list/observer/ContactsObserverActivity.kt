package ru.snowadv.app_contacts.presentation.contact_list.observer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.app_contacts.R
import ru.snowadv.app_contacts.databinding.ActivityContactsBinding
import ru.snowadv.app_contacts.presentation.common.ObserverActivity
import ru.snowadv.app_contacts.presentation.contact_list.adapter.ContactsAdapter
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsActivityEvent
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsUiEvent
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsViewModel
import ru.snowadv.app_contacts.presentation.contact_obtainer.ObtainerActivity
import ru.snowadv.app_contacts.presentation.model.Contact

internal class ContactsObserverActivity : ObserverActivity<ActivityContactsBinding, ContactsViewModel> {

    override fun registerObservingActivity(
        binding: ActivityContactsBinding,
        viewModel: ContactsViewModel,
        activity: ComponentActivity
    ) {
        initListeners(binding, viewModel)
        observeViewModel(binding, viewModel, activity)
    }

    private fun initListeners(binding: ActivityContactsBinding, viewModel: ContactsViewModel) {
        binding.getContactsButton.setOnClickListener {
            viewModel.event(ContactsUiEvent.FetchContactsButtonClicked)
        }
    }

    private fun observeViewModel(
        binding: ActivityContactsBinding,
        viewModel: ContactsViewModel,
        activity: ComponentActivity
    ) {
        val contactsAdapter = observeContacts(binding.contactsList)

        val obtainerActivityLauncher = activity.createObtainerActivityLauncher(
            onContactsReceived = {
                viewModel.event(
                    ContactsUiEvent.FetchedNewContacts(
                        contacts = it
                    )
                )
            },
            onObtainCancelledOrFailed = {
                viewModel.event(ContactsUiEvent.ObtainCancelledOrFailed)
            }
        )

        val requestContactsPermissionLauncher = createRequestContactsPermissionLauncher(
            activity = activity,
            onGranted = {
                viewModel.event(ContactsUiEvent.FetchContactsPermissionAllowed)
            },
            onDenied = {
                viewModel.event(ContactsUiEvent.FetchContactsPermissionDenied)
            }
        )

        viewModel.activityEventFlow.onEach {
            when (it) {
                is ContactsActivityEvent.OpenObtainerActivity -> {
                    obtainerActivityLauncher.launch(
                        Intent(
                            activity,
                            ObtainerActivity::class.java
                        )
                    )
                }

                is ContactsActivityEvent.AskContactsPermission -> {
                    activity.obtainPermissionAndProceed(
                        requestPermissionLauncher = requestContactsPermissionLauncher,
                        permission = Manifest.permission.READ_CONTACTS,
                        rationale = activity.getString(R.string.need_contacts_to_show_list)
                    ) {
                        viewModel.event(ContactsUiEvent.FetchContactsPermissionAllowed)
                    }
                }

                is ContactsActivityEvent.ShowNoPermissionToast -> {
                    activity.showUnableToProceedWithoutPermissionToast()
                }

                is ContactsActivityEvent.ShowObtainCancelledToast -> {
                    activity.showContactsFetchFailedToast()
                }
            }
        }.launchIn(activity.lifecycleScope)
        viewModel.state.onEach {
            contactsAdapter.setContacts(it.contacts)
        }.launchIn(activity.lifecycleScope)
    }

    private fun observeContacts(recyclerView: RecyclerView): ContactsAdapter {
        ContactsAdapter(emptyList()).apply {
            recyclerView.adapter = this
            return this
        }
    }

    private fun createRequestContactsPermissionLauncher(
        activity: ComponentActivity,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }
    }

    private fun ComponentActivity.createObtainerActivityLauncher(
        onContactsReceived: (contacts: List<Contact>) -> Unit,
        onObtainCancelledOrFailed: () -> Unit
    ): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.extras?.let {
                        onContactsReceived(
                            getContactsFromBundle(
                                it,
                                ObtainerActivity.OBTAINER_RESULT_BUNDLE_KEY
                            )
                        )
                    } ?: run {
                        onObtainCancelledOrFailed()
                    }
                }

                Activity.RESULT_CANCELED -> {
                    onObtainCancelledOrFailed()
                }
            }
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

    private fun ComponentActivity.showUnableToProceedWithoutPermissionToast() {
        Toast.makeText(
            this,
            getString(R.string.cant_proceed_without_permission), Toast.LENGTH_LONG
        ).show()
    }

    private fun ComponentActivity.showContactsFetchFailedToast() {
        Toast.makeText(this, getString(R.string.error_while_fetching_contacts), Toast.LENGTH_LONG)
            .show()
    }

    private inline fun ComponentActivity.obtainPermissionAndProceed(
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

    private fun ComponentActivity.showRationaleDialog(
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
}