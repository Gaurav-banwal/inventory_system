package com.oops.inventory_system

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
//not in use
class ItemAdapter(private val items: MutableList<Item>, private val context: Context) : BaseAdapter() {
    private val db = FirebaseFirestore.getInstance()

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = items[position].trackId.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val item = items[position]

        val nameText = view.findViewById<TextView>(R.id.itemName)
        val quantityText = view.findViewById<TextView>(R.id.quantity)
        val categoryText = view.findViewById<TextView>(R.id.itemCategory)
        val locationText = view.findViewById<TextView>(R.id.itemLocation)
        val priceText = view.findViewById<TextView>(R.id.itemPrice)
        val buyPriceText = view.findViewById<TextView?>(R.id.itemBuyPrice)
        val addButton = view.findViewById<Button>(R.id.addButton)
        val subtractButton = view.findViewById<Button>(R.id.subtractButton)

        nameText.text = item.name
        quantityText.text = item.quantity.toString()
        categoryText.text = item.category
        locationText.text = item.location
        priceText.text = "₹${item.price}"
        if (buyPriceText != null) {
            buyPriceText.text = "CP: ₹${item.buyprice}"
        }

        // Critical: Make sure the buttons don't interfere with list item clicks
        addButton.setOnClickListener { v ->
            // Prevent event from propagating to the list item
            v.isClickable = true
            
            // Store original quantity in case update fails
            val originalQuantity = item.quantity
            item.quantity++
            quantityText.text = item.quantity.toString()
            
            // Direct Firestore update
            val docRef = db.collection("inventory").document(item.trackId.toString())
            docRef.update("quantity", item.quantity)
                .addOnSuccessListener {
                    // Update successful
                    Toast.makeText(context, "Updated quantity for ${item.name}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Update failed, revert to original quantity
                    item.quantity = originalQuantity
                    quantityText.text = item.quantity.toString()
                    Toast.makeText(context, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            
            notifyDataSetChanged()
        }

        subtractButton.setOnClickListener { v ->
            // Prevent event from propagating to the list item
            v.isClickable = true
            
            if (item.quantity > 0) {
                // Store original quantity in case update fails
                val originalQuantity = item.quantity
                item.quantity--
                quantityText.text = item.quantity.toString()
                
                // Direct Firestore update
                val docRef = db.collection("inventory").document(item.trackId.toString())
                docRef.update("quantity", item.quantity)
                    .addOnSuccessListener {
                        // Update successful
                        Toast.makeText(context, "Updated quantity for ${item.name}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Update failed, revert to original quantity
                        item.quantity = originalQuantity
                        quantityText.text = item.quantity.toString()
                        Toast.makeText(context, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                
                notifyDataSetChanged()
            }
        }
        
        // Make sure these buttons don't consume the click event for the list item
        addButton.isFocusable = false
        subtractButton.isFocusable = false

        return view
    }
}