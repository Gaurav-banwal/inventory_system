package com.oops.inventory_system

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        // Apply window insets safely
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        mainLayout?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        Toast.makeText(this, "Details Activity Loaded!", Toast.LENGTH_SHORT).show()

        val detailTextView: TextView = findViewById(R.id.itemname)
        val itemDetails = intent.getStringExtra("item_details") ?: "No Details Available"
        Toast.makeText(this, "Received: $itemDetails", Toast.LENGTH_SHORT).show() // Debugging
        detailTextView.text = itemDetails

        // Back button handling
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish() // Properly closes the activity
        }
    }
}