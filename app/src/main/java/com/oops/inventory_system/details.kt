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

        // Get all item details from intent
        val itemName = intent.getStringExtra("item_name") ?: "Unknown Item"
        val itemId = intent.getStringExtra("item_id") ?: "N/A"
        val itemQuantity = intent.getStringExtra("item_quantity") ?: "0"
        val itemCategory = intent.getStringExtra("item_category") ?: "N/A"
        val itemLocation = intent.getStringExtra("item_location") ?: "N/A"
        val itemPrice = intent.getStringExtra("item_price") ?: "0"
        val itemBuyPrice = intent.getStringExtra("item_buyprice") ?: "0"

        // Set the data to the TextViews
        findViewById<TextView>(R.id.itemname).text = itemName
        findViewById<TextView>(R.id.productId).text = "Product ID: $itemId"
        findViewById<TextView>(R.id.itemQuantity).text = "Quantity: $itemQuantity"
        findViewById<TextView>(R.id.itemLocation).text = "Location: $itemLocation"
        findViewById<TextView>(R.id.marketPrice).text = "Price: ₹$itemPrice"
        findViewById<TextView>(R.id.costPrice).text = "Cost Price: ₹$itemBuyPrice"

        // Display category in the title or elsewhere if needed
        supportActionBar?.title = "$itemName Details"
        
        // Back button handling
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish() // Properly closes the activity
        }
    }
}