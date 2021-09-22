package com.decagon.android.sq007.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.Contact
// import com.decagon.android.sq007.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // private var _binding: ActivityMainBinding? = null     //bind to activity_main..xml
    // private val binding get() = _binding!!

    val READ_CONTACTS_RQ = 101
    val CALL_PHONE_RQ = 102

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        checkForPermissions(android.Manifest.permission.READ_CONTACTS, "contacts", READ_CONTACTS_RQ)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) { // get which option was clicked
            R.id.itemOne -> {
                val intent = Intent(this, PermissionSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.readcontacts -> {
                val intent = Intent(this, MainActivitySecond::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //region permission stuff

    //region check for permissions
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                // if permission granted
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    // Toast.makeText(applicationContext,"$name permission granted",Toast.LENGTH_SHORT).show()
                    //getContactList()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }
    //endregion

    // if permissions granted or not, do the follow
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission not granted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PermissionSettingsActivity::class.java)
                startActivity(intent)
            } else {
                // Toast.makeText(applicationContext,"$name permission granted",Toast.LENGTH_SHORT).show()

                // call getphonecontacts
                //getContactList()
            }
        }

        when (requestCode) {
            READ_CONTACTS_RQ -> innerCheck("contacts")
            CALL_PHONE_RQ -> innerCheck("making calls")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    //endregion

    private fun getContactList() {

        // val ISOPrefix: String = getCountryISO()
        val phones: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (phones != null) {
            while (phones.moveToNext()) {
                val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phone = phone.replace(" ", "")
                phone = phone.replace("-", "")
                phone = phone.replace("(", "")
                phone = phone.replace(")", "")
//                if (phone[0].toString() != "+") {
//                    phone = ISOPrefix + phone
//                }

                val contact = Contact()
                contact.fullName = name
                contact.contactNumber = phone

                viewModel.addContact(contact)
            }
        }
    }
}
