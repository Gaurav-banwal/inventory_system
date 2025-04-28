package com.oops.inventory_system

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class selection : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val itemsList = mutableListOf<Item>()
    private lateinit var adapter: ItemListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection)

        val category = intent.getStringExtra("category_name")
        Toast.makeText(this, "Selected Category: $category", Toast.LENGTH_SHORT).show()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            supportActionBar?.title = category
            window.statusBarColor = ContextCompat.getColor(this, R.color.brandgreen)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up ListView with a custom adapter for items
        val listView = findViewById<ListView>(R.id.listview)
        adapter = ItemListAdapter(this, itemsList)
        listView.adapter = adapter

        // Set up search functionality
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) return false
                val trimmed = query.trim()
                // Try to find by ID
                val byId = itemsList.find { it.trackId.toString() == trimmed }
                if (byId != null) {
                    openItemDetails(byId)
                    return true
                }
                // Try to find by name (case-insensitive)
                val byName = itemsList.find { it.name.equals(trimmed, ignoreCase = true) }
                if (byName != null) {
                    openItemDetails(byName)
                    return true
                }
                // Try partial match
                val partial = itemsList.find { it.name.contains(trimmed, ignoreCase = true) }
                if (partial != null) {
                    openItemDetails(partial)
                    return true
                }
                Toast.makeText(this@selection, "Item not found", Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterItems(newText)
                return true
            }
        })

        // Get category from intent and fetch items from Firestore
        category?.let { 
            fetchItemsByCategory(it) 
        }

        // Handle item clicks
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = itemsList[position]
            openItemDetails(selectedItem)
        }
    }

    private fun fetchItemsByCategory(category: String) {
        // Show loading indicator
        Toast.makeText(this, "Loading items...", Toast.LENGTH_SHORT).show()
        
        db.collection("inventory")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { documents ->
                itemsList.clear()
                
                if (documents.isEmpty) {
                    Toast.makeText(this, "No items found in this category", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        try {
                            val trackId = (document.get("trackId") as? Number)?.toInt() ?: 0
                            val name = document.getString("name") ?: "Unknown"
                            val quantity = (document.get("quantity") as? Number)?.toInt() ?: 0
                            val itemCategory = document.getString("category") ?: ""
                            val location = document.getString("location") ?: ""
                            val price = (document.get("price") as? Number)?.toInt() ?: 0
                            val buyprice = (document.get("buyprice") as? Number)?.toInt() ?: 0
                            
                            val item = Item(
                                trackId = trackId,
                                quantity = quantity,
                                name = name,
                                category = itemCategory,
                                location = location,
                                price = price,
                                buyprice = buyprice
                            )
                            
                            itemsList.add(item)
                        } catch (e: Exception) {
                            // Log or handle errors for individual items
                            continue
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Found ${itemsList.size} items", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading items: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openItemDetails(item: Item) {
        val intent = Intent(this, Details::class.java).apply {
            putExtra("item_name", item.name)
            putExtra("item_id", item.trackId.toString())
            putExtra("item_quantity", item.quantity.toString())
            putExtra("item_category", item.category)
            putExtra("item_location", item.location)
            putExtra("item_price", item.price.toString())
            putExtra("item_buyprice", item.buyprice.toString())
        }
        startActivity(intent)
    }

    private fun filterItems(query: String?) {
        if (query.isNullOrEmpty()) {
            // If query is empty, show all items for the category
            adapter.updateItems(itemsList)
            return
        }
        
        // Filter the items based on the query
        val filteredList = itemsList.filter { item ->
            item.name.contains(query, ignoreCase = true) || 
            item.trackId.toString() == query ||
            item.category.contains(query, ignoreCase = true) ||
            item.location.contains(query, ignoreCase = true)
        }
        
        // Update the adapter with filtered list
        adapter.updateItems(filteredList)
    }
}