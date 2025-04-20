package com.oops.inventory_system

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditItem : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    
    private var trackId: Int = 0
    private var name: String = ""
    private var category: String = ""
    private var quantity: Int = 0
    private var location: String = ""
    private var price: Int = 0

    companion object {
        private const val ARG_TRACK_ID = "track_id"
        private const val ARG_NAME = "name"
        private const val ARG_CATEGORY = "category"
        private const val ARG_QUANTITY = "quantity"
        private const val ARG_LOCATION = "location"
        private const val ARG_PRICE = "price"
        
        fun newInstance(trackId: Int, name: String, category: String, quantity: Int, location: String, price: Int): EditItem {
            val fragment = EditItem()
            val args = Bundle().apply {
                putInt(ARG_TRACK_ID, trackId)
                putString(ARG_NAME, name)
                putString(ARG_CATEGORY, category)
                putInt(ARG_QUANTITY, quantity)
                putString(ARG_LOCATION, location)
                putInt(ARG_PRICE, price)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackId = it.getInt(ARG_TRACK_ID)
            name = it.getString(ARG_NAME) ?: ""
            category = it.getString(ARG_CATEGORY) ?: ""
            quantity = it.getInt(ARG_QUANTITY)
            location = it.getString(ARG_LOCATION) ?: ""
            price = it.getInt(ARG_PRICE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_item, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val height = Resources.getSystem().displayMetrics.heightPixels * 0.8
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the title
        dialog?.setTitle("Edit Item")
        
        // Find all views
        val trackIdField = view.findViewById<EditText>(R.id.editTrackId)
        val nameField = view.findViewById<EditText>(R.id.editName)
        val categoryField = view.findViewById<EditText>(R.id.editCategory)
        val quantityField = view.findViewById<EditText>(R.id.editQuantity)
        val locationField = view.findViewById<EditText>(R.id.editLocation)
        val priceField = view.findViewById<EditText>(R.id.editPrice)
        val updateButton = view.findViewById<Button>(R.id.updateButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        
        // Populate fields with existing item data
        trackIdField.setText(trackId.toString())
        nameField.setText(name)
        categoryField.setText(category)
        quantityField.setText(quantity.toString())
        locationField.setText(location)
        priceField.setText(price.toString())
        
        // Make trackId field read-only since it's the document ID
        trackIdField.isEnabled = false
        
        // Handle button clicks
        updateButton.setOnClickListener {
            // Validate inputs
            val newName = nameField.text.toString().trim()
            val newCategory = categoryField.text.toString().trim()
            val newQuantityStr = quantityField.text.toString().trim()
            val newLocationStr = locationField.text.toString().trim()
            val newPriceStr = priceField.text.toString().trim()
            
            if (newName.isEmpty() || newCategory.isEmpty() || newQuantityStr.isEmpty() || 
                newLocationStr.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val newQuantity = newQuantityStr.toIntOrNull() ?: -1
            val newPrice = newPriceStr.toIntOrNull() ?: -1
            
            if (newQuantity < 0 || newPrice < 0) {
                Toast.makeText(context, "Quantity and price must be valid numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Update the item in Firestore
            updateItem(newName, newCategory, newQuantity, newLocationStr, newPrice)
        }
        
        cancelButton.setOnClickListener {
            dismiss()
        }
    }
    
    private fun updateItem(newName: String, newCategory: String, newQuantity: Int, newLocation: String, newPrice: Int) {
        val itemRef = db.collection("inventory").document(trackId.toString())
        
        val updates = hashMapOf<String, Any>(
            "name" to newName,
            "category" to newCategory,
            "quantity" to newQuantity,
            "location" to newLocation,
            "price" to newPrice
        )
        
        itemRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error updating item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
} 