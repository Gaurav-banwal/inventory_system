package com.oops.inventory_system

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    // Add a new inventory item
    fun addItem(trackId: Int, name: String, quantity: Int, location: String, price: Int, category: String, onComplete: (Boolean, Exception?) -> Unit) {
        val itemData = hashMapOf(
            "trackId" to trackId,
            "name" to name,
            "quantity" to quantity,
            "location" to location,
            "price" to price,
            "category" to category
        )

        db.collection("inventory").document(trackId.toString())
            .set(itemData)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e) }
    }

    // Get an inventory item by track ID
    fun getItem(trackId: String, onResult: (DocumentSnapshot?) -> Unit) {
        db.collection("inventory").document(trackId)
            .get()
            .addOnSuccessListener { document -> onResult(document) }
            .addOnFailureListener { e -> onResult(null) }
    }

    // Update inventory item
    fun updateItem(trackId: String, updatedData: Map<String, Any>, onComplete: (Boolean, Exception?) -> Unit) {
        db.collection("inventory").document(trackId)
            .update(updatedData)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e) }
    }

    // Delete inventory item
    fun deleteItem(trackId: String, onComplete: (Boolean, Exception?) -> Unit) {
        db.collection("inventory").document(trackId)
            .delete()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e) }
    }

    // Real-time inventory updates listener
    fun listenToItem(trackId: String, onUpdate: (DocumentSnapshot?) -> Unit) {
        db.collection("inventory").document(trackId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onUpdate(null)
                    return@addSnapshotListener
                }
                onUpdate(snapshot)
            }
    }
}