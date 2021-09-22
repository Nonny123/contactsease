package com.decagon.android.sq007.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R


class PermissionSettingsActivity : AppCompatActivity() {

    val READ_CONTACTS_RQ = 101
    val CALL_PHONE_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        buttonTaps()
    }

    private fun buttonTaps() {
        findViewById<Button>(R.id.btnReadContacts).setOnClickListener { view ->
            checkForPermissions(android.Manifest.permission.READ_CONTACTS, "read contacts", READ_CONTACTS_RQ)
        }

        findViewById<Button>(R.id.btnMakeCalls).setOnClickListener { view ->
            checkForPermissions(android.Manifest.permission.CALL_PHONE, "make calls", CALL_PHONE_RQ)
        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()

                // call getphonecontacts
            }
        }

        when (requestCode) {
            READ_CONTACTS_RQ -> innerCheck("read contacts")
            CALL_PHONE_RQ -> innerCheck("making calls")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@PermissionSettingsActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
