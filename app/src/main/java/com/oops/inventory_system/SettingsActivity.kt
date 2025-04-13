package com.oops.inventory_system

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val profileText: TextView = findViewById(R.id.yourProfile)
        val aboutUsText: TextView = findViewById(R.id.aboutUs)

        profileText.setOnClickListener {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Profile screen later
        }

        aboutUsText.setOnClickListener {
            val aboutMessage = """
        This app is developed by Team OOPS.
        It's designed to manage inventory with ease for students and small businesses.
        
        Version: 1.0.0
        Contact: team.oops@example.com
    """.trimIndent()

            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("About Us")
            builder.setMessage(aboutMessage)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }
}
