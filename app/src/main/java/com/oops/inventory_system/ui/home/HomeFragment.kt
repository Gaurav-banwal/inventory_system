package com.oops.inventory_system.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oops.inventory_system.Details
import com.oops.inventory_system.R
import com.oops.inventory_system.databinding.FragmentHomeBinding
import com.oops.inventory_system.selection

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize SearchView
        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search action
                query?.let {
                    if (it.trim().isNotEmpty()) {
                        performSearch(it.trim())
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Not implementing live search to avoid too many Firestore queries
                return false
            }
        })

        return root
    }

    //search in home bar
    private fun performSearch(query: String) {
        Toast.makeText(context, "Searching for: $query", Toast.LENGTH_SHORT).show()
        
        // First try to search by trackId
        if (query.toIntOrNull() != null) {
            // If query is a number, try to find by trackId
            searchByTrackId(query)
        } else {
            // Otherwise search by name
            searchByName(query)
        }
    }
    //search by id
    private fun searchByTrackId(trackId: String) {
        db.collection("inventory").document(trackId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Item found, open details
                    openItemDetails(document)
                } else {
                    // No item found with this ID, try name search as fallback
                    searchByName(trackId)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Search failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    //search by name
    private fun searchByName(name: String) {
        db.collection("inventory")
            .whereEqualTo("name", name)  // Exact match first
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Exact match found
                    openItemDetails(documents.documents[0])
                } else {
                    // Try partial match if no exact match found
                    searchByPartialName(name)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Search failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    //partial name , sometimes lazyness
    private fun searchByPartialName(name: String) {
        // Since Firestore doesn't support direct partial string search,
        // we'll get all inventory items and filter on the app itself
        db.collection("inventory")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(context, "No items found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                
                // Find first item with name containing search term (case insensitive)
                val matchingDocument = documents.find { 
                    val itemName = it.getString("name") ?: ""
                    itemName.contains(name, ignoreCase = true) //checking here for partial string
                }
                //if found
                if (matchingDocument != null) {
                    openItemDetails(matchingDocument)
                } else {//if not
                    Toast.makeText(context, "No items found matching: $name", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Search failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    //opening details
    private fun openItemDetails(document: com.google.firebase.firestore.DocumentSnapshot) {
        try {//fetching from firebase
            val trackId = (document.get("trackId") as? Number)?.toInt() ?: 0
            val name = document.getString("name") ?: "Unknown"
            val quantity = (document.get("quantity") as? Number)?.toInt() ?: 0
            val category = document.getString("category") ?: ""
            val location = document.getString("location") ?: ""
            val price = (document.get("price") as? Number)?.toInt() ?: 0
            //transfering it to details
            val intent = Intent(requireContext(), Details::class.java).apply {
                putExtra("item_name", name)
                putExtra("item_id", trackId.toString())
                putExtra("item_quantity", quantity.toString())
                putExtra("item_category", category)
                putExtra("item_location", location)
                putExtra("item_price", price.toString())
            }
            startActivity(intent)
            
            Toast.makeText(context, "Found item: $name", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error processing item: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beverages = view.findViewById<LinearLayout>(R.id.beverages)
        val noodles = view.findViewById<LinearLayout>(R.id.noodles)
        val dairy = view.findViewById<LinearLayout>(R.id.dairy)
        val confectioneries = view.findViewById<LinearLayout>(R.id.confectioneries)
        val snack = view.findViewById<LinearLayout>(R.id.snacks)
        val grocery = view.findViewById<LinearLayout>(R.id.groceries)

        beverages.setOnClickListener {
            openCategory("Beverages")
        }

        noodles.setOnClickListener {
            openCategory("Noodles")
        }

        dairy.setOnClickListener {
            openCategory("Dairy")
        }
        confectioneries.setOnClickListener {
            openCategory("confectioneries")
        }
        snack.setOnClickListener {
            openCategory("snacks")
        }
        grocery.setOnClickListener {
            openCategory("Groceries")
        }
    }
//fucntion to open categroy
    private fun openCategory(category: String) {
        val intent = Intent(requireContext(), selection::class.java)
        intent.putExtra("category_name", category) // Pass category name
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//
//
//
//Hello World
//
//
//
//
//
//
//
//
//
//
//
//
