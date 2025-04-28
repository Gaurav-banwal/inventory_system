package com.oops.inventory_system

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class SellItem : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_sell_item, container, false)
        
        val trackIdEditText = view.findViewById<EditText>(R.id.trackIdEditText)
        val quantityEditText = view.findViewById<EditText>(R.id.quantityEditText)
        val sellButton = view.findViewById<Button>(R.id.sellButton)
        
        sellButton.setOnClickListener {
            val trackId = trackIdEditText.text.toString().toIntOrNull()
            val quantity = quantityEditText.text.toString().toIntOrNull()
            
            if (trackId == null || quantity == null) {
                Toast.makeText(context, "Please enter valid values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Get the item details
            db.collection("inventory")
                .document(trackId.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val currentQuantity = (document.get("quantity") as? Number)?.toInt() ?: 0
                        val price = (document.get("price") as? Number)?.toInt() ?: 0
                        val buyprice = (document.get("buyprice") as? Number)?.toInt() ?: 0
                        
                        if (currentQuantity < quantity) {
                            Toast.makeText(context, "Not enough items in stock", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        
                        // Calculate profit
                        val profit = (price - buyprice) * quantity
                        
                        // Update quantity
                        val newQuantity = currentQuantity - quantity
                        db.collection("inventory")
                            .document(trackId.toString())
                            .update("quantity", newQuantity)
                            .addOnSuccessListener {
                                // Update profit in Firestore
                                updateProfit(profit)
                                Toast.makeText(context, "Item sold successfully", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error updating quantity: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error finding item: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        
        return view
    }
    
    private fun updateProfit(profit: Int) {
        db.collection("profit_data")
            .document("current_profit")
            .get()
            .addOnSuccessListener { document ->
                val currentProfit = if (document.exists()) {
                    (document.get("totalProfit") as? Number)?.toInt() ?: 0
                } else {
                    0
                }
                
                val newProfit = currentProfit + profit
                val profitData = mapOf(
                    "totalProfit" to newProfit,
                    "lastUpdated" to System.currentTimeMillis()
                )
                
                db.collection("profit_data")
                    .document("current_profit")
                    .set(profitData)
            }
    }
} 