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
            Toast.makeText(this, "About Us clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to About Us screen later
        }
    }
}
