package com.decagon.android.sq007.ui

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.Contact

class AddEditActivity : AppCompatActivity() {

    var edit_mode = false
    var contactId: String? = null
    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_activity)
        // setSupportActionBar(toolbar)

//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_close_white))
//        }

        contactId = "Edit"

        if (contactId == null) {
            supportActionBar?.title = "Add contact"
        } else {
            edit_mode = true
            supportActionBar?.title = "Edit contact"

            getIncomingIntent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuLayout = if (edit_mode)
            R.menu.menu_edit
        else {
            R.menu.menu_add_edit
        }

        this.menu = menu

        menuInflater.inflate(menuLayout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_update -> {
                // saveButtonClickListener()
                Toast.makeText(this, "Updated !!", Toast.LENGTH_SHORT).show()
                updateContact()
            }
            R.id.action_delete -> {
                // deleteButtonClickListener()
                Toast.makeText(this, "Deleted !!", Toast.LENGTH_SHORT).show()
                deleteContact()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }




    private fun updateContact() {
        val tvPhone = findViewById<TextView>(R.id.txtContactNumber)
        val tvName = findViewById<TextView>(R.id.txtFullName)
        var contact = Contact()
        contact.fullName = tvName.text.toString()
        contact.contactNumber = tvPhone.text.toString()

        //viewModel.updateContact(contact)


    }

    private fun deleteContact(){
        val tvPhone = findViewById<TextView>(R.id.txtContactNumber)
        val tvName = findViewById<TextView>(R.id.txtFullName)
        var contact = Contact()
        contact.fullName = tvName.text.toString()
        contact.contactNumber = tvPhone.text.toString()
        //viewModel.deleteContact(currentContact)
    }

    private fun getIncomingIntent() {

        if (intent.hasExtra("name") && intent.hasExtra("phone")) {

            val nameExtra = intent.getStringExtra("name")
            val phoneExtra = intent.getStringExtra("phone")
            if (nameExtra != null && phoneExtra != null) {
                getContactDetails(nameExtra, phoneExtra)
            }
        }
    }

    private fun getContactDetails(nameextra: String, phoneextra: String) {

        val fullNames = findViewById<TextView>(R.id.txtFullName)
        fullNames.text = nameextra
        val contactNumber = findViewById<TextView>(R.id.txtContactNumber)
        contactNumber.text = phoneextra
    }

    fun saveButtonClickListener() {
        // presenter.onSaveClicked()
    }

    fun deleteButtonClickListener() {
        contactId?.let {
            // presenter.onDeleteClicked(it)
        }
    }

    fun closeView() {
//        val intent = Intent(this, ListActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
    }
}
