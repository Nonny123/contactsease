package com.decagon.android.sq007.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.RecyclerViewContactBinding

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var contacts = mutableListOf<Contact>()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context // initialise the context
        return ViewHolder(RecyclerViewContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewName.text = contacts[position].fullName
        holder.binding.textViewContact.text = contacts[position].contactNumber

        holder.binding.contactcontatiner.setOnClickListener {

            // Toast.makeText(context, contacts[position].contactNumber, Toast.LENGTH_LONG).show()

            val intent = Intent(context, ContactDetailsActivity::class.java)
            intent.putExtra("name", contacts[position].fullName)
            intent.putExtra("phone", contacts[position].contactNumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun addContact(contact: Contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        } else {
            val index = contacts.indexOf(contact)
            if (contact.isDeleted) {
                contacts.removeAt(index)
            } else {
                contacts[index] = contact
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RecyclerViewContactBinding) : RecyclerView.ViewHolder(binding.root)
}
