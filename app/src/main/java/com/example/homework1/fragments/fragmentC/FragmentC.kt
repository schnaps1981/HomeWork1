package com.example.homework1.fragments.fragmentC

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework1.R
import com.example.homework1.models.PhoneContact
import kotlinx.android.synthetic.main.fragment_c.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentC : Fragment(R.layout.fragment_c) {

    private val rvAdapter = ContactListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        if (isReadContactsPermitted())
            getContacts()
        else
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSIONS_CODE
            )

        gotoFragmentA.setOnClickListener {
            navController.navigate(R.id.fragmentA)
        }

    }

    private fun isReadContactsPermitted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED)
                    getContacts()
            }

            else -> Toast.makeText(requireContext(), "permissions not granted", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getContacts() {
        val contactItems = arrayListOf<PhoneContact>()

        CoroutineScope(Dispatchers.Main).launch {
            progressBar.isVisible = true

            withContext(Dispatchers.IO) {
                val contentResolver = requireContext().contentResolver
                val mainCursor: Cursor? = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null
                )

                mainCursor?.let { cursor ->
                    if (cursor.count > 0) {
                        while (cursor.moveToNext()) {
                            val id =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                            val name =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                            val hasNumber =
                                cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0

                            if (hasNumber) {
                                val phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    arrayOf(id),
                                    null
                                )

                                phoneCursor?.let {
                                    while (phoneCursor.moveToNext()) {
                                        val phoneNo: String = phoneCursor.getString(
                                            phoneCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER
                                            )
                                        )
                                        contactItems.add(PhoneContact(name = name, phone = phoneNo))

                                    }
                                }
                                phoneCursor?.close()
                            }
                        }
                    }

                    mainCursor.close()
                }
            }
            progressBar.isVisible = false
            rvAdapter.setList(contactItems)
        }
    }

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 1234

        @JvmStatic
        fun newInstance() = FragmentC()
    }
}
