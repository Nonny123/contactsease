package com.decagon.android.sq007.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.FragmentUpdateContactDialogBinding

class UpdateContactDialogFragment(private val contact: Contact) : DialogFragment() {

    private var _binding: FragmentUpdateContactDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editTextFullName.setText(contact.fullName)
        binding.editTextContact.setText(contact.contactNumber)

        binding.buttonUpdate.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextContact.text.toString().trim()

            if (fullName.isEmpty()) {
                binding.editTextFullName.error = "This field is required"
                return@setOnClickListener
            }

            if (contactNumber.isEmpty()) {
                binding.editTextContact.error = "This field is required"
                return@setOnClickListener
            }

            contact.fullName = fullName
            contact.contactNumber = contactNumber

            viewModel.updateContact(contact)
            dismiss() // this the frament dialog
            Toast.makeText(context, "Contact has been updated", Toast.LENGTH_SHORT).show()
        }
    }
}
