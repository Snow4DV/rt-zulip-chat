package ru.snowadv.app_contacts.presentation.contact_list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.snowadv.app_contacts.R
import ru.snowadv.app_contacts.databinding.ActivityContactsBinding
import ru.snowadv.app_contacts.presentation.common.ObserverActivity
import ru.snowadv.app_contacts.presentation.contact_list.observer.ContactsObserverActivity
import ru.snowadv.app_contacts.presentation.contact_list.viewmodel.ContactsViewModel

internal class ContactsActivity : AppCompatActivity(),
    ObserverActivity<ActivityContactsBinding, ContactsViewModel> by ContactsObserverActivity() {

    private lateinit var binding: ActivityContactsBinding

    private val viewModel by viewModels<ContactsViewModel>()

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

        registerObservingActivity(binding, viewModel, this)
    }
}