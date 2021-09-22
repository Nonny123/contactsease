package com.decagon.android.sq007.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.data.NODE_CONTACTS
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class ContactViewModel : ViewModel() {

    // set connection to database
    private val dbcontacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)

    //region create livedata for data adding error or success monitoring
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result
    //endregion

    //region create livedata for contact adding error or success monitoring
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> get() = _contact
    //endregion

    fun addContact(contact: Contact) {
        contact.id = dbcontacts.push().key // generate id

        dbcontacts.child(contact.id!!).setValue(contact)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // if successful set the value of livedata to null
                    _result.value = null
                } else {
                    // set the error message in the livedata
                    _result.value = it.exception
                }
            }
    }

    fun updateContact(contact: Contact) {
        dbcontacts.child(contact.id!!).setValue(contact)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // if successful set the value of livedata to null
                    _result.value = null
                } else {
                    // set the error message in the livedata
                    _result.value = it.exception
                }
            }
    }

    fun deleteContact(contact: Contact) {
        dbcontacts.child(contact.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // if successful set the value of livedata to null
                    _result.value = null
                } else {
                    // set the error message in the livedata
                    _result.value = it.exception
                }
            }
    }

    // to know if a new contact has been added
    private val childEventListener = object : ChildEventListener {
        // tracking new contact added
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java) // get a contact object from the snapshot of the database
            contact?.id = snapshot.key // get the id of each contact

            // add contact object in the mutable live data, so we can track if contact has been saved
            _contact.value = contact!! // contact!! - contact cannot ne null
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java) // get a contact object from the snapshot of the database
            contact?.id = snapshot.key // get the id of each contact

            // add contact object in the mutable live data, so we can track if contact has been saved
            _contact.value = contact!! // contact!! - contact cannot ne null
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contact = snapshot.getValue(Contact::class.java) // get a contact object from the snapshot of the database
            contact?.id = snapshot.key // get the id of each contact

            contact?.isDeleted = true
            // add contact object in the mutable live data, so we can track if contact has been saved
            _contact.value = contact!! // contact!! - contact cannot ne null
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    // add childeventlistener object to the database to track adding
    fun getRealtimeUpdate() {
        dbcontacts.addChildEventListener(childEventListener)
    }

    // remove childeventlistener once done
    override fun onCleared() {
        super.onCleared()
        dbcontacts.removeEventListener(childEventListener)
    }
}
