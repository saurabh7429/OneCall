package com.onecall.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Contact(val name: String, val number: String)

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private lateinit var rvContacts: RecyclerView
    private lateinit var layoutPermissionDenied: View
    private lateinit var btnGrantPermission: Button
    private lateinit var searchContacts: SearchView
    private lateinit var progressContacts: View
    
    private val contactsList = mutableListOf<Contact>()
    private val adapter = ContactsAdapter(contactsList) { contact ->
        (requireActivity() as? com.onecall.MainActivity)?.initiateOutgoingCall(contact.number, contact.name)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                loadContacts()
            } else {
                showPermissionDenied()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvContacts = view.findViewById(R.id.rv_contacts)
        layoutPermissionDenied = view.findViewById(R.id.layout_permission_denied)
        btnGrantPermission = view.findViewById(R.id.btn_grant_permission)
        searchContacts = view.findViewById(R.id.search_contacts)
        progressContacts = view.findViewById(R.id.progress_contacts)

        rvContacts.layoutManager = LinearLayoutManager(requireContext())
        rvContacts.adapter = adapter

        btnGrantPermission.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }

        searchContacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText.orEmpty())
                return true
            }
        })

        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            showPermissionDenied()
        }
    }

    private fun showPermissionDenied() {
        rvContacts.visibility = View.GONE
        progressContacts.visibility = View.GONE
        layoutPermissionDenied.visibility = View.VISIBLE
    }

    private fun loadContacts() {
        layoutPermissionDenied.visibility = View.GONE
        progressContacts.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val contacts = fetchDeviceContacts()
            withContext(Dispatchers.Main) {
                progressContacts.visibility = View.GONE
                rvContacts.visibility = View.VISIBLE
                contactsList.clear()
                contactsList.addAll(contacts.sortedBy { it.name })
                adapter.updateList(contactsList)
            }
        }
    }

    private fun fetchDeviceContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            
            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                if (number.isNotBlank()) {
                    contacts.add(Contact(name, number.replace(" ", "")))
                }
            }
        }
        return contacts.distinctBy { it.number } // Avoid exact duplicates
    }

    private fun filterContacts(query: String) {
        val lowerQuery = query.lowercase()
        val filtered = contactsList.filter {
            it.name.lowercase().contains(lowerQuery) || it.number.contains(query)
        }
        adapter.updateList(filtered)
    }
}

class ContactsAdapter(
    private var contacts: List<Contact>,
    private val onCallClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_contact_name)
        val tvNumber: TextView = view.findViewById(R.id.tv_contact_number)
        val btnCall: ImageButton = view.findViewById(R.id.btn_contact_call)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.tvName.text = contact.name
        holder.tvNumber.text = contact.number
        holder.btnCall.setOnClickListener { onCallClick(contact) }
    }

    override fun getItemCount() = contacts.size

    fun updateList(newList: List<Contact>) {
        contacts = newList
        notifyDataSetChanged()
    }
}
