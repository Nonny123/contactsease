package com.decagon.android.sq007.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.FragmentAddContactDialogBinding
import com.google.android.material.textfield.TextInputEditText

class AddContactDialogFragment : DialogFragment() {

    private var _binding: FragmentAddContactDialogBinding? = null // bind to fragment_add_contact_dialog.xml
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel
//    private lateinit var edit_text_contact: TextInputEditText
//    private lateinit var button_add: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root


        //intialise views
        //edit_text_contact = _binding!!.editTextContact
        //button_add = _binding!!.buttonAdd


        //validate phone number  during runtime
        //validatePhoneNumber()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //region observe livedata for error or success
        viewModel.result.observe(viewLifecycleOwner) {
            val message = if (it == null) {
                getString(R.string.added_contact)
            } else {
                getString(R.string.error, it.message)
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            dismiss()
        }
        //endregion

        binding.buttonAdd.setOnClickListener {
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

            if (!ValidateRegistration.validatePhoneNumber(contactNumber)) {
                binding.editTextContact.error = "Invalid Nigerian Phone Number"
                return@setOnClickListener
            }

            val contact = Contact()
            contact.fullName = fullName
            contact.contactNumber = contactNumber

            viewModel.addContact(contact)
        }
    }

     //region Validate Phone Number as Input Changes
//    private fun validatePhoneNumber() {
//
//        edit_text_contact.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (ValidateRegistration.validatePhoneNumber(edit_text_contact.text.toString())) {
//                    button_add.isEnabled = true
//
//                } else {
//                    edit_text_contact.error = "Invalid Nigerian Number Format"
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (ValidateRegistration.validatePhoneNumber(edit_text_contact.text.toString())) {
//                    button_add.isEnabled = true
//                } else {
//                    edit_text_contact.error = "Invalid Nigerian Number Format"
//                }
//            }
//
//        })
//    }
    //endregion
}
