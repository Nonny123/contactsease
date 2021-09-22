package com.decagon.android.sq007.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null // bind to fragment_contacts.xml
    private val binding get() = _binding!!

    private val adapter = ContactAdapter()

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewContacts.adapter = adapter

        binding.addButton.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "")
        }

        viewModel.contact.observe(
            viewLifecycleOwner,
            Observer {
                adapter.addContact(it)
            }
        )

        viewModel.getRealtimeUpdate()

        // handle swiping
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)
    }

    // for swiping
    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) { // set to 0 because we don't want to move up and down
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true // because we don't want to move up and down
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition // get the contact that was swiped
            var currentContact = adapter.contacts[position]

            when (direction) {
                ItemTouchHelper.RIGHT -> {
                    UpdateContactDialogFragment(currentContact).show(childFragmentManager, "")
                }
                ItemTouchHelper.LEFT -> {
                    AlertDialog.Builder(requireContext()).also {
                        it.setTitle("Are you sure you want to delete this contact?")
                        it.setPositiveButton("Yes") { dialog, which ->
                            viewModel.deleteContact(currentContact)
                            binding.recyclerViewContacts.adapter?.notifyItemRemoved(position)
                            Toast.makeText(context, "Contact has been deleted", Toast.LENGTH_SHORT).show()
                        }
                    }.create().show()
                }
            }
            binding.recyclerViewContacts.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
