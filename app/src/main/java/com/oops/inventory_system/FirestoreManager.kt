package com.oops.inventory_system

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    // Add a new inventory item
    fun addItem(trackId: String, category: String, price: Int, location: String, quantity: Int, onComplete: (Boolean, Exception?) -> Unit) {
        val itemData = hashMapOf(
            "trackId" to trackId,
            "category" to category,
            "price" to price,
            "location" to location,
            "quantity" to quantity
        )

        db.collection("inventory").document(trackId)
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