package com.onecall.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.databinding.FragmentContactsBinding
import com.onecall.databinding.ItemContactBinding
import com.onecall.service.OneCallService

data class Contact(val name: String, val number: String)

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val contacts = mutableListOf<Contact>()
    private val filteredContacts = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactAdapter(filteredContacts) { contact ->
            OneCallService.instance?.requestOutgoingCall(contact.number)
        }

        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ContactsFragment.adapter
        }

        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) = filterContacts(s?.toString() ?: "")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loadContacts()
    }

    private fun loadContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            binding.tvNoContacts.visibility = View.VISIBLE
            binding.tvNoContacts.text = "Contact permission not granted"
            return
        }

        contacts.clear()
        try {
            val cursor: Cursor? = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            cursor?.use {
                val nameCol = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numCol = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    contacts.add(Contact(it.getString(nameCol), it.getString(numCol)))
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error loading contacts", Toast.LENGTH_SHORT).show()
        }

        filteredContacts.clear()
        filteredContacts.addAll(contacts)
        adapter.notifyDataSetChanged()
        binding.tvNoContacts.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun filterContacts(query: String) {
        filteredContacts.clear()
        if (query.isBlank()) {
            filteredContacts.addAll(contacts)
        } else {
            contacts.filterTo(filteredContacts) {
                it.name.contains(query, ignoreCase = true) || it.number.contains(query)
            }
        }
        adapter.notifyDataSetChanged()
        binding.tvNoContacts.visibility = if (filteredContacts.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ContactAdapter(
    private val contacts: List<Contact>,
    private val onCall: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.binding.tvContactName.text = contact.name
        holder.binding.tvContactNumber.text = contact.number
        holder.binding.tvContactInitial.text = contact.name.firstOrNull()?.uppercase() ?: "?"
        holder.binding.btnCallContact.setOnClickListener { onCall(contact) }
    }

    override fun getItemCount() = contacts.size
}
