package com.decagon.android.sq007.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ContactDetailsActivity : AppCompatActivity() {

    var contactId = ""
    val READ_CONTACTS_RQ = 101
    val CALL_PHONE_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_details)

        contactId = intent.getStringExtra("phone").toString()

        // retrieve input from intent
        getIncomingIntent()

        val layoutPhone = findViewById<LinearLayout>(R.id.phoneList)
        layoutPhone.setOnClickListener {
            checkForPermissions(android.Manifest.permission.CALL_PHONE, "dial your contacts", CALL_PHONE_RQ)
        }

        val shareButton = findViewById<FloatingActionButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            shareContact()
        }
    }

    private fun shareContact(){
        val name = findViewById<TextView>(R.id.tvname)
        val phone = findViewById<TextView>(R.id.tvphone)
        val shareIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, "$name\n$phone" )
            this.type = "text/plain"
        }
        startActivity(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.action_edit) {
            goToEditActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToEditActivity() {
        val tvPhone = findViewById<TextView>(R.id.tvphone)
        val tvName = findViewById<TextView>(R.id.tvname)

        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("name", tvName.text.toString())
        intent.putExtra("phone", tvPhone.text.toString())
        startActivity(intent)
    }

    private fun getIncomingIntent() {

        if (intent.hasExtra("name") && intent.hasExtra("phone")) {

            val nameExtra = intent.getStringExtra("name")
            val phoneExtra = intent.getStringExtra("phone")
            if (nameExtra != null && phoneExtra != null) {
                setDetails(nameExtra, phoneExtra)
            }
        }
    }

    private fun setDetails(nameextra: String, phoneextra: String) {

        val tvPhone = findViewById<TextView>(R.id.tvphone)
        tvPhone.text = phoneextra
        val tvName = findViewById<TextView>(R.id.tvname)
        tvName.text = nameextra
    }

    private fun makeCall() {
        val number = findViewById<TextView>(R.id.tvphone)
        val callIntent = Intent(Intent.ACTION_CALL) // Intent.ACTION_DIAL
        callIntent.data = Uri.parse("tel:${number.text}")
        startActivity(callIntent)
    }

    //region permission stuff
    //region check for permissions
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    // Toast.makeText(applicationContext,"$name permission granted", Toast.LENGTH_SHORT).show()

                    // make call
                    makeCall()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }
    //endregion

    //region if permissions granted or not
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission not granted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PermissionSettingsActivity::class.java)
                startActivity(intent)
            } else {
                // Toast.makeText(applicationContext,"$name permission granted",Toast.LENGTH_SHORT).show()

                // make call
                makeCall()
            }
        }

        when (requestCode) {
            READ_CONTACTS_RQ -> innerCheck("contacts")
            CALL_PHONE_RQ -> innerCheck("making calls")
        }
    }
    //endregion

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@ContactDetailsActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    //endregion
}
